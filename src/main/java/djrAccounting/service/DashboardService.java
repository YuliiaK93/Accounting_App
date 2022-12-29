package djrAccounting.service;

import djrAccounting.dto.currency.UsdDto;
import djrAccounting.dto.dashboard.FinancialSummaryDto;

public interface DashboardService {
    FinancialSummaryDto financialSummaryForCurrentCompany();

    UsdDto getExchangeRates();
}
