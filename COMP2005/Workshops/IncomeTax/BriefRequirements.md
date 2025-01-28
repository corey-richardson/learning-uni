# Workshop Exercise: Income Tax

You are required to build a Desktop Java application (using IntelliJ) that:
- Allows users to calculate deductions (such as income tax) from their current (or potential) salary

This is quite a vague brief (deliberately so)

You should go through the SW development lifecycle (a very short 'micro' version)

What would you ask the user to further understand requirements?

Fabricate (and record) some answers to these questions to allow you to proceed with development.

---

## Fabricated Requirements

The application should be a command line interface allowing the user to input their current or a potential salary (`double`), and it will output a value with tax deducted.

The application should use the UK Income Tax Rates and Bands.

Band               |Taxable income       | Tax rate
---                | ---                 | ---
Personal Allowance | Up to £12,570       | 0%
Basic rate         | £12,571 to £50,270  | 20%
Higher rate        | £50,271 to £125,140 | 40%
Additional rate    | over £125,140	     | 45%

The UK GOV.UK site, [Estimate your Income Tax for the current year
](https://www.gov.uk/estimate-income-tax), could be used to initially compare results to expected values.
 