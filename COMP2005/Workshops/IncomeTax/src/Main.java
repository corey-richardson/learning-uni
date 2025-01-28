import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        double PERSONAL_UPPER = 12570.00;
        double BASIC_LOWER = 12571.00;
        double BASIC_UPPER = 50270.00;
        double HIGHER_LOWER = 50271.00;
        double HIGHER_UPPER = 125140.00;
        double ADDITIONAL_LOWER = 125140.00;

        System.out.println("What is your salary?");
        double salary, intermediate, deductions = 0;
        try {
            salary = Double.parseDouble(reader.readLine());
        } catch (Error | IOException e) {
            throw new RuntimeException(e);
        }

        double salary_copy = salary;
        System.out.printf("%,.2f\n", salary);

        // Additional Rate
        if (salary >= ADDITIONAL_LOWER) {
            deductions += 0.45 * (salary - ADDITIONAL_LOWER);
            salary = HIGHER_LOWER;
            PERSONAL_UPPER = 0;
        }

        // Higher Rate
        if (salary >= HIGHER_LOWER) {
            deductions += 0.4 * (salary - HIGHER_LOWER);
            salary = BASIC_UPPER;
        }

        // Basic Rate
        if (salary >= BASIC_LOWER) {
            deductions += 0.2 * (salary - BASIC_LOWER);
            // salary = PERSONAL_UPPER;
        }

        System.out.printf("Post Tax Pay: £%,.2f\n", salary_copy-deductions);
        System.out.printf("Tax Paid:     £%,.2f\n", deductions);
    }
}
