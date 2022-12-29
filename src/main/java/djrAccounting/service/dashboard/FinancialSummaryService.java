package djrAccounting.service.dashboard;

import djrAccounting.dto.dashboard.FinancialSummaryDto;

public interface FinancialSummaryService {

    FinancialSummaryDto financialSummaryForCurrentCompany();
}