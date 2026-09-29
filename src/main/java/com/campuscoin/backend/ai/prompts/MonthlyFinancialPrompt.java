package com.campuscoin.backend.ai.prompts;

public class MonthlyFinancialPrompt {

    private String actualContext;

    public MonthlyFinancialPrompt(String actualContext) {
        this.actualContext = actualContext;
    }

    public String generatePrompt() {

        return """
            Review the student's monthly financial information provided below
            and generate a monthly financial report.

            The report should provide a clear and concise assessment of the
            student's financial activity for the month.

            The summary should:
            - Describe the student's overall financial activity.
            - Highlight meaningful spending patterns and notable observations.
            - Compare actual spending against available budgets where applicable.
            - Identify the student's areas of financial strength.
            - Identify areas where the student could improve.
            - End with a general recommendation based on the overall financial
              situation for the month.

            The tips should provide more detailed and practical guidance based
            on the observations identified in the summary. They should focus on
            specific actions the student can take to improve or maintain their
            financial situation.

            When reviewing budgets, consider whether spending is below, close to,
            or above the available budget for each category.

            If the student's savings goal and expected monthly income are both 0,
            interpret this as the student having no specific savings or income
            expectations. Do not treat this as a problem or make assumptions
            about what their financial goals should be.

            Base the report strictly on the financial information provided.
            Do not invent transactions, income, expenses, budgets, financial
            goals, or other information that is not present in the data.
            
            In your summary and tips, always refer to the student directly using second-person personal pronouns like you

            STUDENT MONTHLY FINANCIAL INFORMATION
            =====================================
            """
                + actualContext
                + """

            Generate the report using the following response structure:

            title:
            A short and meaningful title which includes the month(s) of the report

            summary:
            A concise analysis of the student's financial activity. Include
            important observations, areas of strength, areas of improvement,
            and end with a general recommendation.

            firstTip:
            A detailed and practical recommendation addressing the most important
            area for improvement or financial opportunity identified in the
            analysis.

            secondTip:
            A second detailed and practical recommendation that complements the
            first tip and supports the student's overall financial improvement.

            Return only the requested structured response.
            """;
    }
}
