# 🏦 Tax Return Calculation System — Full UI/UX Redesign Report
**Project 6 | DSAI1004 — Object-Oriented Programming with Java**
**Analysis Date:** 2026-02-19
**Prepared by:** Multi-Agent Analysis Team (Agents 1–4)

---

## Table of Contents
1. [Agent 1 — Project Explorer: Full Codebase Map](#agent-1--project-explorer)
2. [Agent 2 — UI/UX Auditor: Screen-by-Screen Audit](#agent-2--uiux-auditor)
3. [Agent 3 — Code Quality Inspector: Bugs & Logic Issues](#agent-3--code-quality-inspector)
4. [Agent 4 — Redesign Planner: Prioritized Rebuild Plan](#agent-4--redesign-planner)
5. [Final Summary: Issue Severity Matrix](#final-summary)

---

## Agent 1 — Project Explorer

### 1.1 Package & Class Hierarchy

```
src/com/oop/project/
│
├── Main.java                          ← Entry point; calls DataInitializer then LoginFrame
│
├── model/
│   ├── User.java                      ← Two constructors (3-arg legacy + 7-arg full)
│   ├── Client.java                    ← Two constructors (5-arg legacy + 8-arg full)
│   ├── TaxReturn.java                 ← 5 fields; status = "On Time" | "Late"
│   └── TaxBracket.java                ← Parallel arrays; getTaxRate() is flat-rate only
│
├── ui/
│   ├── LoginFrame.java                ← JFrame, 300×200, GridLayout(3,2)
│   ├── MainFrame.java                 ← JFrame, 1000×700, JTabbedPane (4 tabs)
│   ├── DashboardPanel.java            ← JPanel, 3 plain JLabels
│   ├── ClientPanel.java               ← JPanel, form + JTable
│   ├── TaxCalculationPanel.java       ← JPanel, search + calculation + HTML receipt
│   └── ReturnsPanel.java              ← JPanel, read-only JTable from static list
│
├── service/
│   ├── AuthenticationService.java     ← Plain-text password comparison + console logging
│   ├── ClientService.java             ← CRUD + validation (SSN regex)
│   ├── TaxCalculationService.java     ← DEAD CODE — never called by any UI panel
│   └── TaxReturnService.java          ← fileTaxReturn() never called by UI
│
├── repository/
│   ├── UserRepository.java            ← Reads/writes users.dat (binary)
│   ├── ClientRepository.java          ← Reads/writes clients.dat (binary)
│   └── TaxReturnRepository.java       ← Reads/writes taxreturns.dat (binary)
│
├── util/
│   ├── TaxCalculator.java             ← Core calc + HTML receipt (uses US brackets)
│   ├── FileUtil.java                  ← ObjectOutputStream/ObjectInputStream (binary .dat)
│   ├── ValidationUtil.java            ← SSN, email, phone, marital status validators
│   ├── DataInitializer.java           ← Thin wrapper → calls SeedDataInitializer
│   └── SeedDataInitializer.java       ← Creates 10 users + 30 Vietnamese clients
│
└── exception/
    ├── InvalidCredentialException.java
    └── InvalidDataException.java
```

**Total files:** 25 `.java` files, **0** `.form` files (no NetBeans GUI Builder used)
**Data files:** `users.dat`, `clients.dat`, `taxreturns.dat` (binary Java serialization)

### 1.2 Data File Reality Check

| Required (Spec)       | Actual Implementation              | Match? |
|-----------------------|------------------------------------|--------|
| `.txt` sequential files | `.dat` binary serialization files | ❌ NO |
| Pipe-separated (`\|`) text | `ObjectOutputStream` binary format | ❌ NO |
| UTF-8 encoding         | Java default serialization          | ❌ NO |
| `users.txt`            | `users.dat`                        | ❌ NO |
| `clients.txt`          | `clients.dat`                      | ❌ NO |
| `returns.txt`          | `taxreturns.dat`                   | ❌ NO |

### 1.3 Tax Calculation Logic Locations

| Location | What it does | Status |
|---|---|---|
| `TaxCalculator.calculateTax()` | Progressive bracket math (annual) | Used by UI, but **US brackets** |
| `TaxCalculator.generateHTMLReceipt()` | HTML breakdown table | Used by `TaxCalculationPanel` |
| `TaxCalculationService.calculateTax()` | Flat-rate via `TaxBracket` | **Dead code** — never called from UI |
| `TaxBracket.getTaxRate()` | Returns flat rate for bracket | **Not progressive** |

### 1.4 Navigation Flow (Current)

```
Main.java
  └─ DataInitializer.initializeSampleData()
  └─ LoginFrame (300×200)
       └─ [Login Success] → MainFrame (1000×700)
            ├─ Tab 0: ClientPanel
            ├─ Tab 1: TaxCalculationPanel
            ├─ Tab 2: ReturnsPanel ← refreshed on tab switch
            └─ Tab 3: DashboardPanel
```

---

## Agent 2 — UI/UX Auditor

### 2.1 LoginFrame — Audit

**Current state:** 300×200 `JFrame`, `GridLayout(3,2)`, 3 rows × 2 columns

| Issue | Severity | Detail |
|---|---|---|
| Window too small | HIGH | 300×200 is cramped; no room for branding |
| No application logo/banner | MEDIUM | Missing company identity |
| No FlatLaf theme applied | HIGH | Plain Metal LAF appearance |
| No keyboard shortcut (Enter) | MEDIUM | Cannot press Enter to log in |
| Success dialog before window opens | LOW | `JOptionPane` "Login successful!" pops before `MainFrame` appears — jarring UX |
| No "Show/Hide Password" toggle | LOW | Accessibility issue |
| Password sent to empty cell | MEDIUM | `GridLayout(3,2)` → loginButton occupies cell [2,0] but [2,1] is empty |
| Hardcoded English | MEDIUM | Vietnamese consulting company should have Vietnamese UI |
| No input focus on load | LOW | `usernameField` does not auto-focus |
| No branding/version | LOW | No company name, version number |

### 2.2 MainFrame — Audit

**Current state:** 1000×700 `JFrame`, `JTabbedPane` with 4 tabs in wrong order

| Issue | Severity | Detail |
|---|---|---|
| Dashboard is last tab (Tab 3) | HIGH | Dashboard = overview, should be Tab 0 |
| No logged-in user display | HIGH | `currentUser` stored but never shown anywhere in UI |
| No logout button | HIGH | Only way to exit is close window (EXIT_ON_CLOSE) |
| No role-based tab visibility | HIGH | ADMIN vs TAX_STAFF see identical interface |
| No status bar | MEDIUM | No bottom bar showing current user, time, system status |
| Tab labels are English | MEDIUM | "Clients", "Tax Calculation", "Returns", "Dashboard" |
| No toolbar/menu bar | LOW | No `JMenuBar` for File, Help, etc. |
| Fixed window size | LOW | Not resizable; no min/max size constraints |
| `currentUser` passed in but unused | HIGH | Role-based features missing entirely |

### 2.3 DashboardPanel — Audit

**Current state:** `GridLayout(3,1)`, 3 plain `JLabel` objects

| Issue | Severity | Detail |
|---|---|---|
| Only 3 stats, need 6 | CRITICAL | Missing: Total Clients, Filed count, Pending count |
| Plain `JLabel`s instead of stat cards | HIGH | Required design: styled stat cards |
| Uses `$` currency prefix | HIGH | System is Vietnamese — must use VND formatting |
| No Total Tax Collected stat | CRITICAL | Required dashboard stat missing |
| No Average Tax Refund/Owed stat | CRITICAL | Required dashboard stat missing |
| No refresh button | MEDIUM | Stats only update on panel construction |
| `lateFilingsLabel` shows "Late" not "Overdue" | MEDIUM | Status naming inconsistency |
| No visual hierarchy | HIGH | All 3 labels identical size/weight |
| No color coding | MEDIUM | No red for overdue, green for filed |
| No last-updated timestamp | LOW | User cannot tell if data is stale |

### 2.4 ClientPanel — Audit

**Current state:** `BorderLayout`, form NORTH + table CENTER

| Issue | Severity | Detail |
|---|---|---|
| `maritalStatusField` is free-text `JTextField` | HIGH | Should be `JComboBox` (SINGLE/MARRIED/DIVORCED/WIDOWED) |
| Form ignores extended fields | HIGH | No inputs for: dependents, email, phoneNumber, city |
| Table shows only 5 columns | MEDIUM | Missing columns: email, phone, city, dependents |
| Table shows raw `contactInfo` (deprecated) | MEDIUM | Should show `email` instead |
| Income displayed as raw double | MEDIUM | No VND formatting (e.g., `262.14` instead of `262 VND`) |
| Delete has no confirmation dialog | HIGH | One mis-click deletes client permanently |
| Table row click does not fill form | HIGH | Standard UX: clicking a row should populate the edit form |
| Search only by name | MEDIUM | No search by ID, city, or marital status |
| No filter/sort on table | MEDIUM | JTable has no `TableRowSorter` |
| Label says "ID (SSN):" | HIGH | Vietnamese system should say "Mã số thuế" not SSN |
| Label says "Contact Info:" | MEDIUM | Deprecated field — should say "Email" |
| No CSV export button | HIGH | Required feature missing entirely |
| No pagination | LOW | 30+ clients → table scrolls, fine for now |
| `addButton` does not clear form after success | LOW | Already implemented via `clearFields()` ✓ |
| `incomeField` accepts any string | MEDIUM | No input type constraint; NFE is caught but silent until submit |

### 2.5 TaxCalculationPanel — Audit

**Current state:** `BorderLayout`, search NORTH + cards+input+HTML CENTER + file button SOUTH

| Issue | Severity | Detail |
|---|---|---|
| Search label says "Enter Client ID (SSN):" | HIGH | Should say "Mã số thuế khách hàng" |
| Input asks "Annual Income ($):" | HIGH | Uses `$` (USD), should be VND; also inconsistency with monthly stored income |
| Checkbox says "Married Filing Jointly" | HIGH | US IRS concept; Vietnam uses different filing model |
| `btnFileReturn` saves to static list only | CRITICAL | Data lost on restart; repository never called |
| HTML receipt uses `$` and "BRACKET" | HIGH | Should display VND and Vietnamese tax tier names |
| HTML receipt header: "Tax Calculation Statement" | MEDIUM | Should be in Vietnamese |
| Summary cards only 3 (Income, Tax, Net) | MEDIUM | Could add: Effective Rate, Dependent Deduction, Personal Deduction |
| No dependent deduction calculation visible | CRITICAL | Vietnamese tax requires deducting 11M + (4.4M × dependents) before applying brackets |
| No personal deduction input | CRITICAL | UI shows no field for personal deduction |
| `chkMarried` triggers tax bracket change | HIGH | In Vietnamese system, marital status doesn't change brackets — dependents count does |
| `currentClient` NPE risk | HIGH | `currentClient.getMaritalStatus().toUpperCase()` at line 217 — null if maritalStatus is null |

### 2.6 ReturnsPanel — Audit

**Current state:** `BorderLayout`, "Refresh Data" button NORTH + JTable CENTER

| Issue | Severity | Detail |
|---|---|---|
| Data source is static in-memory list | CRITICAL | `TaxCalculationPanel.savedReturns` — not persisted |
| No search/filter on table | HIGH | Required feature missing |
| No CSV export | HIGH | Required feature missing |
| No status filter (Filed/Pending/Overdue) | HIGH | Core use case missing |
| Status values wrong | HIGH | Shows "On Time"/"Late" not "Filed"/"Pending"/"Overdue" |
| No ability to add/edit/delete returns | MEDIUM | Read-only view |
| Tax liability formatted correctly | LOW | Uses `TaxCalculator.formatCurrency()` ✓ |
| `clientName = "Unknown"` fallback | MEDIUM | Silently shows "Unknown" — no visual alert |
| No column sorting | MEDIUM | No `TableRowSorter` |
| Tightly coupled to `TaxCalculationPanel` | HIGH | Architectural smell — panel reads from another panel's static field |

---

## Agent 3 — Code Quality Inspector

### 3.1 Critical Bugs

#### BUG-01 (CRITICAL): Wrong Tax Brackets — US System Instead of Vietnamese
**File:** `TaxCalculator.java`, lines 12–19

```java
// CURRENT (WRONG — US IRS 2023 rates, USD thresholds):
private static final double[] SINGLE_THRESHOLDS = {0, 11000, 44725, 95375, 182100, 231250, 578125};
private static final double[] SINGLE_RATES      = {0.10, 0.12, 0.22, 0.24, 0.32, 0.35, 0.37};

// REQUIRED (Vietnamese progressive 7-tier, VND/month):
// Bracket 1: ≤ 5,000,000 VND/month → 5%
// Bracket 2: 5M–10M → 10%
// Bracket 3: 10M–18M → 15%
// Bracket 4: 18M–32M → 20%
// Bracket 5: 32M–52M → 25%
// Bracket 6: 52M–80M → 30%
// Bracket 7: > 80M → 35%
```

**Impact:** Every single tax calculation in the system is mathematically wrong for Vietnamese users.

---

#### BUG-02 (CRITICAL): Tax Return Data Never Persisted to File
**File:** `TaxCalculationPanel.java`, line 19 + lines 279–280

```java
// Line 19 — static in-memory list:
public static List<TaxReturn> savedReturns = new ArrayList<>();

// Line 279-280 — adds to list, never calls repository:
TaxReturn newReturn = new TaxReturn(clientId, filingDate, taxLiability, status, maritalStatusStr);
savedReturns.add(newReturn);  // ← NEVER written to taxreturns.dat
```

**Impact:** All filed tax returns are lost when application restarts. `TaxReturnRepository.addTaxReturn()` exists but is never called.

---

#### BUG-03 (CRITICAL): File I/O Implementation Contradicts Academic Spec
**File:** `FileUtil.java`, lines 9–28

```java
// ACTUAL: Binary Java Object Serialization
new ObjectOutputStream(new FileOutputStream(fileName))
new ObjectInputStream(new FileInputStream(fileName))
// → produces .dat files, NOT human-readable .txt files

// REQUIRED: Sequential text I/O with pipe-separated values
// e.g., BufferedWriter/BufferedReader writing:
// 903-73-9276|Nguyen Ngan An|262.14|3|MARRIED|email|phone|city
```

**Impact:** Violates "Sequential File I/O with plain text `.txt` files" academic requirement. Data files cannot be inspected or manually edited. UTF-8 Vietnamese characters may corrupt in binary format.

---

#### BUG-04 (CRITICAL): Missing Vietnamese Deductions in Tax Calculation
**File:** `TaxCalculator.java` — entire file

The Vietnamese tax formula is:
```
Taxable Income = Monthly Income − 11,000,000 (personal deduction) − (4,400,000 × dependents)
Annual Taxable Income = max(0, Taxable Income) × 12
Then apply progressive brackets to Annual Taxable Income
```

Current implementation applies brackets directly to gross income with no deductions. For a client with income 10M VND/month and 2 dependents:
- **Correct:** Taxable = 10M − 11M − 8.8M = negative → tax = 0 VND
- **Current code:** Tax = 10M × 10% = 1,000,000 VND (completely wrong)

---

#### BUG-05 (HIGH): TaxCalculationService is Dead Code with Wrong Logic
**File:** `TaxCalculationService.java`, lines 10–23

```java
// Uses wrong thresholds AND flat-rate (not progressive):
double[] thresholds = {10000, 40000, 80000, 160000, Double.MAX_VALUE};
double[] rates = {0.10, 0.12, 0.22, 0.24, 0.32};
// calculateTax: return income * rate  ← FLAT RATE, not progressive
```

**Additionally:** `TaxBracket.getTaxRate()` returns a single flat rate.  `TaxReturnService.fileTaxReturn()` calls this service — but `TaxReturnService.fileTaxReturn()` is itself never called from any UI class.

---

#### BUG-06 (HIGH): Null Pointer Risk in TaxCalculationPanel
**File:** `TaxCalculationPanel.java`, line 217

```java
String realStatus = currentClient.getMaritalStatus().toUpperCase();
// If currentClient.getMaritalStatus() returns null → NullPointerException
```

No null-check before calling `.toUpperCase()`.

---

#### BUG-07 (HIGH): Tax Filing Deadline Hardcoded to US Date (April 15)
**File:** `TaxCalculationPanel.java` line 276; `TaxReturnService.java` line 37

```java
// CURRENT (US IRS deadline):
LocalDate deadline = LocalDate.of(filingDate.getYear(), 4, 15);

// REQUIRED (Vietnam):
LocalDate deadline = LocalDate.of(filingDate.getYear(), 4, 30);
```

---

#### BUG-08 (HIGH): Income Values in Seed Data Are Inconsistent with Tax System
**File:** `SeedDataInitializer.java`, lines 86–145

Client incomes are stored as e.g. `262.14`, `1326.52`, `2728.87`. These appear to be in USD or some unspecified unit, NOT millions of VND. For Vietnamese tax:
- A monthly income of `262.14 VND` is essentially zero (below poverty line)
- A monthly income of `262.14 million VND` = 262,140,000 VND (very high earner)

The seed data does not specify currency unit, making tax calculations meaningless.

---

#### BUG-09 (MEDIUM): Wrong ID Format — SSN Instead of Vietnamese Tax ID
**File:** `ValidationUtil.java` lines 9–11; `ClientService.java` line 53

```java
// US SSN format validation:
public static boolean isValidSSN(String ssn) {
    return ssn != null && ssn.matches("\\d{3}-\\d{2}-\\d{4}");
}
// And ClientService validates: !client.getId().matches("\\d{3}-\\d{2}-\\d{4}")
```

Vietnamese Mã số thuế (Tax ID) format: 10–13 digits, no dashes. `ValidationUtil.isValidTaxID()` exists (10-digit check) but is never used — `SSN` validation is used instead.

---

#### BUG-10 (MEDIUM): Plain-Text Password Storage
**File:** `AuthenticationService.java`, line 16; `User.java`, line 10

```java
if (user == null || !user.getPassword().equals(password)) { ... }
// Comment: "In real app, this should be hashed"
```

Passwords stored in plaintext in `users.dat`. Even for academic purposes, this is a security concern that should at least use `MessageDigest.getInstance("SHA-256")`.

---

#### BUG-11 (MEDIUM): `FileUtil` Silently Swallows Errors
**File:** `FileUtil.java`, lines 14, 24

```java
} catch (IOException e) {
    e.printStackTrace();  // ← prints to console, not shown to user
}
} catch (IOException | ClassNotFoundException e) {
    e.printStackTrace();  // ← same issue
}
```

If `clients.dat` is corrupted, the UI shows an empty table with no explanation. User sees no error.

---

#### BUG-12 (MEDIUM): `ClientPanel` Uses Deprecated Old Constructor
**File:** `ClientPanel.java`, lines 96–98 and 113–115

```java
// Uses 5-arg old constructor — ignores dependents, phone, city, email:
Client client = new Client(idField.getText(), nameField.getText(),
    Double.parseDouble(incomeField.getText()), maritalStatusField.getText(),
    contactField.getText());
```

New clients added via UI will have `dependents=0`, `phoneNumber=""`, `city=""` regardless of what the user intended.

---

#### BUG-13 (LOW): `AuthenticationService.logLogin()` Uses `System.out.println`
**File:** `AuthenticationService.java`, lines 23–28

Login/logout events logged to console only — no persistent audit log file. Console output won't exist after deployment.

---

### 3.2 Separation of Concerns Violations

| Location | Violation |
|---|---|
| `TaxCalculationPanel.java` line 19 | Business data (`savedReturns`) stored as `public static` field in UI class |
| `ReturnsPanel.java` line 42 | UI panel directly reads another UI panel's static field |
| `TaxCalculationPanel.java` lines 269–284 | `fileTaxReturn()` performs business logic (date comparison, status determination) that belongs in `TaxReturnService` |
| `ClientPanel.java` lines 43–44 | Uses raw `client.getMaritalStatus()` string for `maritalStatus` field — no validation at UI layer |
| `TaxCalculator.java` lines 131–206 | HTML generation mixed with tax math in same utility class |

---

### 3.3 Missing Features (Functional Gaps)

| Required Feature | Status |
|---|---|
| 6-stat dashboard cards | ❌ Only 3 stats exist |
| CSV export | ❌ Not implemented anywhere |
| Filing status: Filed / Pending / Overdue | ❌ Only "On Time" / "Late" |
| Role-based views (Admin vs Staff) | ❌ `currentUser` never used for access control |
| Vietnamese tax deductions (personal + dependent) | ❌ Not implemented |
| `JTable` filtering/searching | ❌ Missing on all panels |
| Tax return persistence across sessions | ❌ In-memory only |
| Plain text `.txt` file I/O | ❌ Binary serialization used instead |
| Vietnamese currency formatting (VND) | ❌ Uses `$` USD everywhere |
| FlatLaf theme configuration | ❌ Never set up in any entry point |

---

## Agent 4 — Redesign Planner

### 4.1 Priority Tiers

**TIER 1 — Must Fix Before Demo (Correctness)**
| # | Item | Files Affected |
|---|---|---|
| P1-1 | Replace US tax brackets with Vietnamese 7-tier system | `TaxCalculator.java` |
| P1-2 | Add Vietnamese deductions (personal + dependents) to calculation | `TaxCalculator.java`, `TaxCalculationPanel.java` |
| P1-3 | Migrate File I/O from binary serialization to UTF-8 pipe-separated `.txt` | `FileUtil.java`, all 3 repositories |
| P1-4 | Wire `TaxReturnRepository` into `TaxCalculationPanel.fileTaxReturn()` | `TaxCalculationPanel.java`, `TaxReturnService.java` |
| P1-5 | Fix filing deadline from April 15 → April 30 | `TaxCalculationPanel.java`, `TaxReturnService.java` |
| P1-6 | Fix seed data income values with proper VND amounts | `SeedDataInitializer.java` |
| P1-7 | Add null guard for `getMaritalStatus()` in TaxCalculationPanel | `TaxCalculationPanel.java:217` |

**TIER 2 — Dashboard & Statistics (Feature Completeness)**
| # | Item | Files Affected |
|---|---|---|
| P2-1 | Rebuild `DashboardPanel` with 6 stat cards | `DashboardPanel.java` (full rebuild) |
| P2-2 | Add "Filed" / "Pending" / "Overdue" status enum/constants | `TaxReturn.java`, `TaxReturnService.java` |
| P2-3 | VND currency formatting throughout all panels | `TaxCalculator.java`, all UI panels |

**TIER 3 — UI/UX Polish (User Experience)**
| # | Item | Files Affected |
|---|---|---|
| P3-1 | Redesign `LoginFrame` (bigger, branded, FlatLaf) | `LoginFrame.java` |
| P3-2 | Add user info bar + logout to `MainFrame` | `MainFrame.java` |
| P3-3 | Move Dashboard to Tab 0 in `MainFrame` | `MainFrame.java` |
| P3-4 | Upgrade `ClientPanel` form (JComboBox, new fields, row-click, confirmation) | `ClientPanel.java` |
| P3-5 | Add CSV export to `ClientPanel` and `ReturnsPanel` | Both UI files + new `CsvExporter.java` |
| P3-6 | Add `TableRowSorter` + filter bar to all JTables | `ClientPanel.java`, `ReturnsPanel.java` |
| P3-7 | Add role-based tab visibility | `MainFrame.java` |

**TIER 4 — Architecture (Clean Code)**
| # | Item | Files Affected |
|---|---|---|
| P4-1 | Delete or properly integrate `TaxCalculationService` (dead code) | `TaxCalculationService.java` |
| P4-2 | Move business logic from `TaxCalculationPanel` to `TaxReturnService` | Both files |
| P4-3 | Create `VndFormatter` utility class | New file |
| P4-4 | Apply FlatLaf in `Main.java` | `Main.java` |
| P4-5 | Add audit log file writer to `AuthenticationService` | `AuthenticationService.java` |

---

### 4.2 Screens to Rebuild vs. Refine

| Screen | Action | Reason |
|---|---|---|
| `LoginFrame` | **Rebuild** | Too small, no branding, no FlatLaf |
| `DashboardPanel` | **Rebuild** | Fundamentally incomplete (3 of 6 stats) |
| `TaxCalculationPanel` | **Major refactor** | Wrong calculation engine, no deductions, wrong currency |
| `ClientPanel` | **Moderate refactor** | Missing fields, no JComboBox, no confirmation, no CSV |
| `ReturnsPanel` | **Moderate refactor** | Fix data source, add filter + CSV, fix status values |
| `MainFrame` | **Minor refactor** | Tab reorder, user bar, logout, role-based tabs |

---

### 4.3 Proposed New / Replacement Classes

| Class | Package | Purpose |
|---|---|---|
| `VndFormatter.java` | `util` | Format `double` as `1,500,000 VND`; parse VND string back to double |
| `CsvExporter.java` | `util` | Export `List<Client>` or `List<TaxReturn>` to `.csv` file |
| `VietnameseTaxCalculator.java` | `util` | Replace `TaxCalculator`; uses VND brackets + personal/dependent deductions |
| `AppTheme.java` | `util` | Central FlatLaf config: colors, fonts, border styles |
| `TaxStatus.java` | `model` | Enum: `FILED`, `PENDING`, `OVERDUE` (replaces String literals) |
| `StatCard.java` | `ui` | Reusable stat card component (`JPanel` subclass with title + value + color) |

---

### 4.4 Proposed Color Palette (FlatLaf Theme)

```java
// AppTheme configuration
Primary Blue:     #1565C0   (header, primary buttons)
Accent Green:     #2E7D32   (positive values, "Filed" status)
Alert Red:        #C62828   (overdue, error states, tax amount)
Warning Amber:    #F57F17   (pending, warnings)
Background:       #F5F7FA   (main panel background)
Card White:       #FFFFFF   (stat card background)
Text Primary:     #212121   (main text)
Text Secondary:   #757575   (labels, captions)
Border:           #E0E0E0   (table borders, card borders)
```

**FlatLaf Setup in `Main.java`:**
```java
FlatLightLaf.setup();
UIManager.put("Button.arc", 8);
UIManager.put("Component.arc", 6);
UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));
```

---

### 4.5 Proposed File I/O Format (Plain Text `.txt`)

**`users.txt`** — pipe-separated:
```
NV001|admin|admin123|ADMIN|Nguyen Van Quan Tri|admin@tuvanthe.vn|0901000001
```

**`clients.txt`** — pipe-separated:
```
903-73-9276|Nguyen Ngan An|15000000.00|3|MARRIED|email@gmail.com|0978796918|Quy Nhon
```

**`returns.txt`** — pipe-separated:
```
903-73-9276|2025-04-28|1250000.00|FILED|MARRIED
```

**New `FileUtil` methods:**
```java
void writeLines(List<String> lines, String fileName, String charset)
List<String[]> readPipeSeparated(String fileName, String charset)
```

---

### 4.6 Vietnamese Tax Calculation Algorithm (Corrected)

```
Input: monthlyIncome (VND), dependents (int), maritalStatus (String)

Step 1: Personal deduction = 11,000,000 VND/month
Step 2: Dependent deduction = 4,400,000 × dependents VND/month
Step 3: Monthly taxable = max(0, monthlyIncome − 11,000,000 − (4,400,000 × dependents))
Step 4: Annual taxable = monthlyTaxable × 12

Step 5: Apply 7 progressive brackets to annualTaxable:
  Bracket 1: min(annualTaxable, 60,000,000)           × 5%
  Bracket 2: min(annualTaxable − 60M, 60,000,000)     × 10%  (range 60M–120M)
  Bracket 3: min(annualTaxable − 120M, 96,000,000)    × 15%  (range 120M–216M)
  Bracket 4: min(annualTaxable − 216M, 168,000,000)   × 20%  (range 216M–384M)
  Bracket 5: min(annualTaxable − 384M, 240,000,000)   × 25%  (range 384M–624M)
  Bracket 6: min(annualTaxable − 624M, 336,000,000)   × 30%  (range 624M–960M)
  Bracket 7: max(0, annualTaxable − 960,000,000)      × 35%

Step 6: Annual tax = sum of all bracket taxes
Step 7: Monthly tax = Annual tax / 12
```

---

### 4.7 Recommended Navigation Flow (Redesigned)

```
Main.java
  └─ FlatLaf.setup()
  └─ DataInitializer.initializeSampleData()
  └─ LoginFrame (600×450)
       ├─ [Enter / Button] → authenticate
       └─ [Login Success] → MainFrame (1200×800)
            ├─ Header bar: [Logo] [App Title] [User: Nguyen Van A (ADMIN)] [Logout]
            ├─ Tab 0: Dashboard    ← MOVED FIRST
            ├─ Tab 1: Clients      ← ADMIN + STAFF
            ├─ Tab 2: Tax Filing   ← ADMIN + STAFF
            ├─ Tab 3: Returns      ← ADMIN + STAFF
            └─ Tab 4: Users Mgmt   ← ADMIN ONLY (new tab)
            └─ Status bar: [Last updated: 19/02/2026 10:30] [Records: 30 clients]
```

---

### 4.8 Dashboard Rebuild Specification (6 Stat Cards)

```
┌─────────────────────────────────────────────────────────────┐
│  [Total Clients]   [Filed Returns]   [Pending Returns]      │
│   30               18                 8                     │
│   (blue)           (green)            (amber)               │
├─────────────────────────────────────────────────────────────┤
│  [Overdue Returns] [Total Tax Collected]  [Avg Tax/Client]  │
│   4                 25,600,000 VND         1,280,000 VND    │
│   (red)             (dark blue)            (purple)         │
└─────────────────────────────────────────────────────────────┘
```

Each card: `StatCard(title, value, bgColor, valueColor, icon)`

---

### 4.9 Suggested Folder/Package Restructuring

No major restructuring needed — the existing package layout (`model`, `ui`, `service`, `repository`, `util`, `exception`) is clean. Additions:

```
util/
  ├── VndFormatter.java      ← NEW
  ├── CsvExporter.java       ← NEW
  ├── AppTheme.java          ← NEW
  └── ...existing...

model/
  ├── TaxStatus.java         ← NEW (enum)
  └── ...existing...

ui/
  ├── StatCard.java          ← NEW (reusable component)
  ├── UserManagementPanel.java ← NEW (admin only)
  └── ...existing...
```

---

## Final Summary

### Issue Severity Matrix

| ID | Issue | Severity | Category | File |
|---|---|---|---|---|
| B01 | US tax brackets used instead of Vietnamese | 🔴 CRITICAL | Logic | `TaxCalculator.java` |
| B02 | No Vietnamese deductions in calculation | 🔴 CRITICAL | Logic | `TaxCalculator.java` |
| B03 | Tax returns never saved to file | 🔴 CRITICAL | Data | `TaxCalculationPanel.java` |
| B04 | File I/O uses binary, not plain text | 🔴 CRITICAL | Spec | `FileUtil.java` + repos |
| B05 | Dashboard missing 3 of 6 required stats | 🔴 CRITICAL | Feature | `DashboardPanel.java` |
| B06 | No CSV export anywhere | 🔴 CRITICAL | Feature | All panels |
| B07 | Dead `TaxCalculationService` with wrong math | 🟠 HIGH | Logic | `TaxCalculationService.java` |
| B08 | Filing deadline April 15 not April 30 | 🟠 HIGH | Logic | `TaxCalculationPanel.java` |
| B09 | NullPointerException risk in TaxCalcPanel | 🟠 HIGH | Bug | `TaxCalculationPanel.java:217` |
| B10 | SSN format used instead of Vietnamese Tax ID | 🟠 HIGH | Domain | `ValidationUtil.java` |
| B11 | No FlatLaf theme initialization | 🟠 HIGH | UI | `Main.java` |
| B12 | Dashboard tab last, should be first | 🟠 HIGH | UX | `MainFrame.java` |
| B13 | No user info / logout in MainFrame | 🟠 HIGH | UX | `MainFrame.java` |
| B14 | No role-based access control | 🟠 HIGH | Feature | `MainFrame.java` |
| B15 | All currency in `$` (USD) not VND | 🟠 HIGH | Domain | All UI panels |
| B16 | ClientPanel delete no confirmation | 🟠 HIGH | UX | `ClientPanel.java` |
| B17 | ClientPanel missing extended fields | 🟠 HIGH | Feature | `ClientPanel.java` |
| B18 | Status "On Time"/"Late" vs "Filed"/"Pending"/"Overdue" | 🟠 HIGH | Domain | `TaxReturn.java` |
| B19 | Marital status free-text instead of JComboBox | 🟡 MEDIUM | UX | `ClientPanel.java` |
| B20 | FileUtil swallows IO errors silently | 🟡 MEDIUM | Error handling | `FileUtil.java` |
| B21 | Password stored in plaintext | 🟡 MEDIUM | Security | `User.java` |
| B22 | Table row click doesn't populate form | 🟡 MEDIUM | UX | `ClientPanel.java` |
| B23 | No filter/sort on any JTable | 🟡 MEDIUM | Feature | All panels |
| B24 | LoginFrame too small, no branding | 🟡 MEDIUM | UX | `LoginFrame.java` |
| B25 | Seed data income amounts unit unclear | 🟡 MEDIUM | Data | `SeedDataInitializer.java` |
| B26 | Console.log login audit, not file | 🟢 LOW | Feature | `AuthenticationService.java` |
| B27 | No "Show Password" toggle in login | 🟢 LOW | UX | `LoginFrame.java` |
| B28 | LoginFrame success JOptionPane before MainFrame | 🟢 LOW | UX | `LoginFrame.java` |

### Recommended Implementation Order

```
Phase 1 (Core Correctness)    → B01, B02, B03, B04, B08, B25
Phase 2 (Feature Completeness) → B05, B06, B14, B18
Phase 3 (UI/UX Polish)         → B11, B12, B13, B15, B16, B17, B19, B22, B23, B24
Phase 4 (Code Quality)         → B07, B09, B10, B20, B21, B26, B27, B28
```

---

*Report generated by: Agent 1 (Project Explorer) · Agent 2 (UI/UX Auditor) · Agent 3 (Code Quality Inspector) · Agent 4 (Redesign Planner)*
