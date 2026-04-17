# VTAX — Vietnam Tax Return Management System

## Stack
- **Language:** Java 17, no Maven/Gradle
- **UI:** Java Swing (Nimbus L&F, dark red `#8B1A1A`, custom `AppTheme`)
- **DB:** MySQL via plain JDBC (`DatabaseUtil`) — HikariCP jar exists in `lib/` but is unused
- **Build:** Self-bootstrapping — `Main.java` auto-compiles `src/ → bin/` via `JavaCompiler` API on first run

## Run
```
run.bat          # Windows — compiles then runs
run.ps1          # PowerShell — same
java -cp "lib/*;bin" com.oop.project.Main   # manual after compile
```
Requires JDK (not just JRE). Config in `config.properties` (gitignored, template at `config.properties.example`):
```
db.url=jdbc:mysql://...
db.user=
db.password=
```

## Architecture: 3-Tier MVC
```
ui → service → repository → MySQL
```

| Layer | Package | Classes |
|---|---|---|
| UI | `ui` | `LoginFrame`, `MainFrame`, `DashboardPanel`, `ClientPanel`, `TaxCalculationPanel`, `ReturnsPanel`, `AdministrationPanel` |
| Service | `service` | `AuthenticationService`, `ClientService`, `TaxReturnService` |
| Repository | `repository` | `UserRepository`, `ClientRepository`, `TaxReturnRepository` |
| Model | `model` | `User`, `Client`, `TaxReturn`, `TaxBracket` |
| Config | `config` | `ConfigLoader` |
| Util | `util` | `TaxCalculator`, `ValidationUtil`, `DatabaseUtil`, `DataInitializer`, `AppTheme`, `VndFormatter`, `CsvExporter` |
| Exception | `exception` | `InvalidCredentialException`, `InvalidDataException` |

## Database (MySQL — `tax_return_system`)
| Table | PK | Key columns |
|---|---|---|
| `Users` | `staff_id` | `username` UNIQUE, `password` (plain-text!), `role` (ADMIN/TAX_STAFF) |
| `Clients` | `id` (9-digit tax code) | `name`, `income` (monthly VND), `dependents`, `marital_status` |
| `TaxReturns` | `id` AUTO_INCREMENT | `client_id` FK→Clients CASCADE DELETE, `filing_date`, `tax_liability`, `status` (Filed/Overdue) |

Seed data: `database/seed.sql`. Schema: `database/schema.sql`.

## Key Flows

**Startup:** `Main` → bootstrap/compile if needed → `DataInitializer.initializeSampleData()` (tests DB) → `LoginFrame`

**Login:** `AuthenticationService.authenticate()` → `UserRepository.findUserByUsername()` → throws `InvalidCredentialException` on failure → opens `MainFrame(user)`

**Tax Filing (core):**
1. Enter 9-digit Tax ID → `ClientService.findClientById()` → auto-fill form
2. `DocumentListener` → real-time `TaxCalculator.calculateTax(monthlyIncome, dependents)`
3. Formula: `monthlyTaxable = max(0, income − 11M − 4.4M×dependents)` → `annualTaxable = ×12` → 7-bracket progressive
4. `TaxCalculator.generateHTMLReceipt()` → rendered in `JEditorPane`
5. Save → `TaxReturnService.fileTaxReturn()` → status Filed (≤ Apr 30) or Overdue

**Navigation:** `MainFrame` — `CardLayout` + `Map<String, JButton>` sidebar. "Administration" tab only shown for ADMIN role.

## Vietnamese Tax Brackets (annual taxable income)
| Bracket | Range | Rate |
|---|---|---|
| 1 | ≤ 60M | 5% |
| 2 | 60–120M | 10% |
| 3 | 120–216M | 15% |
| 4 | 216–384M | 20% |
| 5 | 384–624M | 25% |
| 6 | 624–960M | 30% |
| 7 | >960M | 35% |

Logic lives entirely in `TaxCalculator.applyProgressiveBrackets()` (`util/TaxCalculator.java`).

## Known Technical Debt
1. **Passwords plain-text** — `User.password` unhashed (acknowledged in code comments)
2. **No service-layer RBAC** — authorization only enforced by hiding UI tabs
3. **No connection pooling** — `DatabaseUtil` opens a new `DriverManager` connection per call
4. **`System.out/err` println** throughout repositories — no proper logger

## OOP Patterns (academic project — course requirements drive design)
- **Repository Pattern** — all DB access isolated in `*Repository` classes, `PreparedStatement` everywhere (no SQL injection)
- **Service Layer** — UI never touches repositories directly
- **MVC** — model/ui/service separation
- **Utility classes** — `final` + `private` constructor (TaxCalculator, DatabaseUtil, ConfigLoader)
- **CardLayout navigation** — replaces if-else panel switching
- **AbstractTableModel** — `AdministrationPanel.UserTableModel`
- `Serializable` on all model classes

## File Structure (src only)
```
src/com/oop/project/
├── Main.java                    # entry point + self-bootstrap compiler
├── config/ConfigLoader.java     # reads config.properties
├── exception/Invalid*.java
├── model/{User,Client,TaxReturn,TaxBracket}.java
├── repository/{User,Client,TaxReturn}Repository.java
├── service/{Authentication,Client,TaxReturn}Service.java
├── ui/{Login,Main}Frame.java + {Dashboard,Client,TaxCalculation,Returns,Administration}Panel.java
└── util/{TaxCalculator,ValidationUtil,DatabaseUtil,DataInitializer,AppTheme,VndFormatter,CsvExporter}.java
```

## .gitignore
`bin/`, `out/`, `target/`, `*.class`, `config.properties` are all ignored.
