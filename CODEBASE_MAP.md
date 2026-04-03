# CODEBASE MAP — Tax Return Calculation System

## Logo File
- **Path**: `logo.png` (project root)

## Frontend Files (UI + Theme)

### `src/com/oop/project/util/AppTheme.java`
Central theme class — all colors, fonts, and UI factory methods.

**Color Constants:**
| Name | Value | Usage |
|------|-------|-------|
| BACKGROUND | #F4F1EC | General bg |
| CARD_BG | #FFFFFF | Card bg |
| BORDER_COLOR | #E2DDD6 | Borders |
| TEXT_PRIMARY | #1C1C1C | Main text |
| TEXT_SECONDARY | #7A7265 | Labels |
| PRIMARY_BLUE | #1A3A5C | Stat card accent |
| ACCENT_BLUE | #1A5CA8 | Stat card accent |
| ACCENT_GREEN | #1E6B45 | Success btn, stat |
| WARNING_AMBER | #965B00 | Warn btn, hints |
| ALERT_RED | #A52820 | Danger btn, stat |
| LIGHT_BLUE_BG | #DCE8F7 | Selection bg |
| SIDEBAR_BG | #101820 | Sidebar, primary btn |
| SIDEBAR_HOVER | #1A2634 | Nav hover |
| SIDEBAR_ACTIVE | #182638 | Nav selected |
| SIDEBAR_ACCENT | #C8943A | Nav accent bar, brand text |
| SIDEBAR_TEXT | #7A8A9A | Nav text |
| SIDEBAR_DIVIDER | #1E2838 | Sidebar divider |
| WARM_BG | #F4F1EC | Content area bg |
| WARM_BORDER | #E2DDD6 | Content borders |
| WARM_CARD | #FFFFFF | Card bg |
| WARM_STRIPE | #F9F7F4 | Table odd rows |
| WARM_HEADER | #F0EEE9 | Table header bg |
| TABLE_SELECTION_BG | #DCE8F7 | Table selection |
| TABLE_SELECTION_FG | #1C1C1C | Table selection text |
| FILED_TEXT/BG | #1E6B45/#E6F4ED | Status badge |
| PENDING_TEXT/BG | #965B00/#FDF3D8 | Status badge |
| OVERDUE_TEXT/BG | #A52820/#FDE9E7 | Status badge |
| PANEL_HEADER_BG | #101820 | Panel header |
| TEXT_WHITE | #FFFFFF | White text |
| PURPLE | #6A1B9A | Avg tax stat card |

**Key Methods:** `styleTable()`, `primaryBtn()`, `successBtn()`, `dangerBtn()`, `warnBtn()`, `ghostBtn()`, `createStatCard()`, `createStatusLabel()`, `sectionHeader()`

---

### `src/com/oop/project/ui/LoginFrame.java`
- **Top-left**: Left panel brand "VTAX" text + tagline (dark sidebar bg). No logo currently.
- **Layout**: `BorderLayout` with left brand panel (`SIDEBAR_BG` #101820) + right white form panel
- **Colors**: `SIDEBAR_BG`, `SIDEBAR_ACCENT`, `TEXT_PRIMARY`, `TEXT_SECONDARY`, inline hardcoded colors

---

### `src/com/oop/project/ui/MainFrame.java`
- **Sidebar**: brand section (VTAX text), nav items, user info panel
- **Top bar**: white bg, breadcrumb label + clock
- **Status bar**: `WARM_HEADER` bg
- **Top-left**: Sidebar brand "VTAX" text. No logo currently.
- **Colors**: `SIDEBAR_BG`, `SIDEBAR_HOVER`, `SIDEBAR_ACTIVE`, `SIDEBAR_ACCENT`, `SIDEBAR_TEXT`, `WARM_BG`, `WARM_BORDER`, inline hardcoded colors

---

### `src/com/oop/project/ui/DashboardPanel.java`
- **Top-left**: Page header "System Overview" with refresh button
- **Colors**: `WARM_BG`, `PRIMARY_BLUE`, `ACCENT_GREEN`, `WARNING_AMBER`, `ALERT_RED`, `ACCENT_BLUE`, inline `#6A1B9A`
- **Table**: Status badge renderer via `AppTheme.createStatusLabel()`

---

### `src/com/oop/project/ui/ClientPanel.java`
- **Top-left**: Page header "Client Management"
- **Colors**: All via `AppTheme` constants
- **Table**: Row striping via `prepareRenderer`, no status column

---

### `src/com/oop/project/ui/ReturnsPanel.java`
- **Top-left**: Page header "Tax Returns"
- **Colors**: All via `AppTheme` constants
- **Stats chips**: `PRIMARY_BLUE`, `FILED_TEXT`, `PENDING_TEXT`, `OVERDUE_TEXT`
- **Table**: Status badge renderer via `AppTheme.createStatusLabel()`

---

### `src/com/oop/project/ui/TaxCalculationPanel.java`
- **Top-left**: Page header "Tax Filing"
- **Colors**: All via `AppTheme` constants + `SIDEBAR_ACCENT` for step labels
- **Summary cards**: `ACCENT_BLUE`, `ALERT_RED`, `ACCENT_GREEN`

---

### `src/com/oop/project/ui/AdministrationPanel.java`
- **Top-left**: Page header "Administration"
- **Colors**: All via `AppTheme` constants
- **Table**: Actions column with Edit/Delete buttons

---

### `src/com/oop/project/Main.java`
- Uses **Nimbus L&F** (not FlatLaf)
- Sets `control`, `nimbusBase`, `nimbusBlueGrey`, `nimbusFocus` colors
- Calls `DataInitializer.initializeSampleData()`

## Backend Files (DO NOT TOUCH)
| File | Package |
|------|---------|
| `Client.java` | model |
| `TaxBracket.java` | model |
| `TaxReturn.java` | model |
| `User.java` | model |
| `ClientRepository.java` | repository |
| `TaxReturnRepository.java` | repository |
| `UserRepository.java` | repository |
| `AuthenticationService.java` | service |
| `ClientService.java` | service |
| `TaxCalculationService.java` | service |
| `TaxReturnService.java` | service |
| `CsvExporter.java` | util |
| `DataInitializer.java` | util |
| `DatabaseUtil.java` | util |
| `TaxCalculator.java` | util |
| `ValidationUtil.java` | util |
| `VndFormatter.java` | util |
| `ConnectionTester.java` | config |
| `DatabaseConfig.java` | config |
| `InvalidCredentialException.java` | exception |
| `InvalidDataException.java` | exception |
