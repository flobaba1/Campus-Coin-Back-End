package com.campuscoin.backend.service.ai;

import com.campuscoin.backend.ai.contexts.MonthFinancialContext;
import com.campuscoin.backend.ai.dto.MonthlyFinancialSummaryStructure;
import com.campuscoin.backend.ai.prompts.MonthlyFinancialPrompt;
import com.campuscoin.backend.entity.Notification;
import com.campuscoin.backend.enums.NotificationStatus;
import com.campuscoin.backend.enums.NotificationType;
import com.campuscoin.backend.repository.BudgetRepository;
import com.campuscoin.backend.repository.NotificationRepository;
import com.campuscoin.backend.repository.TransactionRepository;
import com.campuscoin.backend.repository.UserRepository;
import com.campuscoin.backend.service.analytics.MonthlyFinancialAnalytics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class MonthlyFinancialSummaryService {
    private static final Logger log =
            LoggerFactory.getLogger(MonthlyFinancialSummaryService.class);

    private static final DateTimeFormatter MONTH_FORMAT =
            DateTimeFormatter.ofPattern("MM-yyyy");

    private final AiService aiService;

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    private final BudgetRepository budgetRepository;

    private final MonthlyFinancialAnalytics monthlyFinancialAnalytics;

    private final NotificationRepository notificationRepository;

    private String systemPrompt = """
        You are a financial assistant helping students understand their personal finances.

        Review the student's monthly financial expenditure and analyze the provided
        financial information. Your goal is to:
        - Summarize the student's financial activity for the month.
        - Identify notable spending patterns.
        - Highlight areas that may require attention.
        - Provide practical tips and advice to help the student manage their finances better.

        Base your analysis only on the financial information provided.
        Do not invent transactions, income, expenses, budgets, or other financial details.

        If the student's savings goal is 0 and expected monthly income is 0,
        interpret this as the student having no specific savings or income expectations.
        Do not criticize or make assumptions about the absence of these expectations.

        Keep your analysis clear, practical, and appropriate for a student.
        """;

    public MonthlyFinancialSummaryService(AiService aiService, UserRepository userRepository, TransactionRepository transactionRepository, BudgetRepository budgetRepository, MonthlyFinancialAnalytics monthlyFinancialAnalytics, NotificationRepository notificationRepository) {
        this.aiService = aiService;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.monthlyFinancialAnalytics = monthlyFinancialAnalytics;
        this.notificationRepository = notificationRepository;
    }

    public boolean generateMonthReport(String userId, int month, int year){
        MonthFinancialContext context = monthlyFinancialAnalytics.generateContext(userId, year, month);
        MonthlyFinancialPrompt prompt = new MonthlyFinancialPrompt(context.generateContext());
        log.info("Context : {}\n Prompt: {}", context.generateContext(), prompt.generatePrompt());
        MonthlyFinancialSummaryStructure resp =  aiService.generateStructured(systemPrompt, prompt.generatePrompt(), MonthlyFinancialSummaryStructure.class);
        pushNotificationsToUser(resp, userId);

        return true;
    }

    public boolean getMyReport(String userId, List<String> months) {

        if (months == null || months.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one month must be provided"
            );
        }

        StringBuilder combinedContext = new StringBuilder();

        for (String monthValue : months) {

            YearMonth yearMonth;

            try {
                yearMonth = YearMonth.parse(monthValue, MONTH_FORMAT);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException(
                        "Invalid month format: " + monthValue
                                + ". Expected format is MM-yyyy"
                );
            }

            int month = yearMonth.getMonthValue();
            int year = yearMonth.getYear();

            MonthFinancialContext context =
                    monthlyFinancialAnalytics.generateContext(
                            userId,
                            year,
                            month
                    );

            log.info(
                    "Generated financial context for user {} for {}",
                    userId,
                    monthValue
            );

            combinedContext
                    .append(context.generateContext())
                    .append("\n\n");
        }
        MonthlyFinancialPrompt prompt = new MonthlyFinancialPrompt(combinedContext.toString());
        log.info(
                "Generating financial report for user {} for {} month(s)",
                userId,
                months.size()
        );

        MonthlyFinancialSummaryStructure report =
                aiService.generateStructured(
                        systemPrompt,
                        prompt.generatePrompt(),
                        MonthlyFinancialSummaryStructure.class
                );

        /*
         * Create the three notifications after the AI report
         * has been successfully generated.
         */
        pushNotificationsToUser(report, userId);

        return true;
    }

    public void pushNotificationsToUser(
            MonthlyFinancialSummaryStructure report,
            String userId
    ) {

        LocalDateTime validUntil =
                LocalDateTime.now().plusDays(30);

        List<Notification> notifications = List.of(

                // Main financial summary
                new Notification(
                        report.title(),
                        report.summary(),
                        validUntil,
                        NotificationType.USER,
                        "ANNOUNCEMENT",
                        null,
                        NotificationStatus.UNREAD,
                        userId
                ),

                // First financial tip
                new Notification(
                        report.title() + " - Tip 1",
                        report.firstTip(),
                        validUntil,
                        NotificationType.USER,
                        "TIPS",
                        null,
                        NotificationStatus.UNREAD,
                        userId
                ),

                // Second financial tip
                new Notification(
                        report.title() + " - Tip 2",
                        report.secondTip(),
                        validUntil,
                        NotificationType.USER,
                        "TIPS",
                        null,
                        NotificationStatus.UNREAD,
                        userId
                )
        );

        notificationRepository.saveAll(notifications);

        log.info(
                "Created {} financial report notifications for user {}",
                notifications.size(),
                userId
        );
    }

}
