package ldcapps.servicehelper.controllers.tools

import javafx.fxml.Initializable
import javafx.print.PageOrientation
import javafx.scene.control.Button
import javafx.scene.control.DatePicker
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text
import ldcapps.servicehelper.*
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.DataClasses.Companion.companies
import ldcapps.servicehelper.db.DataClasses.Companion.user
import ldclibs.javafx.controls.AutoCompletedTextField
import ldclibs.javafx.controls.pickers.BankPicker
import ldclibs.javafx.controls.pickers.PRNPicker
import ldclibs.javafx.controls.pickers.PhonePicker
import java.awt.Toolkit
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CreateContract : Initializable {
    lateinit var pane: AnchorPane
    lateinit var confirmBtn: Button
    lateinit var openBtn: Button
    lateinit var cusFullNameTf: TextField
    lateinit var cusPATf: BankPicker
    lateinit var footingTf: AutoCompletedTextField
    lateinit var cusPRNTf: PRNPicker
    lateinit var dateP: DatePicker
    lateinit var cusInPersonTf: AutoCompletedTextField
    lateinit var cusPhoneTf: PhonePicker
    lateinit var cusEmailTf: TextField
    lateinit var page1: AnchorPane
    lateinit var page2: AnchorPane
    lateinit var dateTx: Text
    lateinit var headerTx: Text
    lateinit var executorTx: Text
    lateinit var exAddressTx: Text
    lateinit var exPATx: Label
    lateinit var exBankAddressTx: Text
    lateinit var exBIKTx: Text
    lateinit var exPRNTx: Text
    lateinit var exPhoneTx: Text
    lateinit var exEmailTx: Text
    lateinit var exDateTx: Text
    lateinit var exFullNameTx: Text
    lateinit var customerTx: Text
    lateinit var cusAddressTx: Text
    lateinit var cusPATx: Label
    lateinit var cusBankAddressTx: Text
    lateinit var cusBIKTx: Text
    lateinit var cusPRNTx: Text
    lateinit var cusPhoneTx: Text
    lateinit var cusEmailTx: Text
    lateinit var cusDateTx: Text

    private var path: String? = null
    private var contract = Contract()

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        initExecutor()

        cusPATf.onClose = { initContract() }
        cusPRNTf.onClose = { initContract() }
        cusPhoneTf.onClose = { initContract() }

        footingTf.items = data.footings
        cusInPersonTf.items = data.inPersons
        pane.prefHeight = Toolkit.getDefaultToolkit().screenSize.height - 25.0
        dateP.value = LocalDate.now()
        confirmBtn.setOnAction {
            if (isNotNull(
                    cusFullNameTf, cusPATf,
                    footingTf, cusPRNTf, cusInPersonTf, cusPhoneTf, cusEmailTf
                )
            ) {
                if (path != null || companies.map { it.company }.find { it == cusPRNTf.company } == null) {
                    try {
                        val date = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(dateP.value)

                        contract = Contract(
                            date, footingTf.text, user.executor, user.abbreviatedExecutor,
                            user.footing, user.address,
                            user.pa, user.bank, user.bankAddress, user.bik, user.prn.toString(), user.phone,
                            user.email, cusPRNTf.company, cusFullNameTf.text, cusInPersonTf.text,
                            cusPRNTf.address, cusPATf.pa, cusPATf.bank, cusPATf.bankAddress, cusPATf.bik,
                            cusPRNTf.prn.toString(), cusPhoneTf.value, cusEmailTf.text
                        )

                        if (path == null) path = Regex("[/*?\"<>|]").replace(
                            "${settings.contractsLocate}\\Договор от $date от ${cusPRNTf.company}.contract",
                            ""
                        )

                        toJSON(path!!, contract)

                        companies.removeIf { it.company == contract.cusCompany }
                        companies.add(
                            DataClasses.Company(
                                cusPRNTf.company, cusPRNTf.address, cusPATf.pa, cusPATf.bank,
                                cusPATf.bik, cusPRNTf.prn.toString(), date
                            )
                        )

                        if (!data.bankAddresses.contains(contract.cusBankAddress)) data.bankAddresses.add(contract.cusBankAddress)
                        if (!data.footings.contains(contract.footing)) data.footings.add(contract.footing)
                        if (!data.inPersons.contains(contract.cusInPerson)) data.inPersons.add(contract.cusInPerson)

                        toJSON(".data", data)

                        Dialogs.confirmation("Договор успешно создан и находится по пути:\n$path\nРаспечатать его?") {
                            Dialogs.print(ToolSelector.toolStage, PageOrientation.PORTRAIT, page1, page2)
                        }
                        Panes.CREATE_CONTRACT.update<CreateContract>()
                    } catch (ex: Exception) {
                        Dialogs.warning("Невозможно создать Договор")
                    }
                } else Dialogs.warning("Данная компания уже есть в базе данных")
            }
        }

        openBtn.setOnAction { loadData(Dialogs.getFile(ToolSelector.toolStage, settings.contractsLocate)) }
    }

    fun initContract() {
        val date = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(dateP.value)

        dateTx.text = date
        headerTx.text =
            "${if (contract.exAbbreviatedCompanyName == null) user.executor else contract.exCompanyName} действующий на основании Свидетельства о государственной регистрации " +
                    "${if (contract.exAbbreviatedCompanyName == null) user.footing else contract.exRegsCert}, " +
                    "именуемый в дальнейшем ИСПОЛНИТЕЛЬ с одной стороны и ${cusPRNTf.company} в лице ${cusInPersonTf.text} ${cusFullNameTf.text}, " +
                    "действующего на основании ${footingTf.text}, именуемое в дальнейшем ЗАКАЗЧИК, с другой стороны, заключили настоящий договор о нижеследующем:"
        exDateTx.text = date
        customerTx.text = cusPRNTf.company
        cusAddressTx.text = cusPRNTf.address
        cusPATx.text = "Р/c: ${cusPATf.pa} в ${cusPATf.bank}"
        cusBankAddressTx.text = cusPATf.bankAddress
        cusBIKTx.text = "БИК: ${cusPATf.bik}"
        cusPRNTx.text = "УНП: ${cusPRNTf.prn}"
        cusPhoneTx.text = "Тел.: ${cusPhoneTf.value}"
        cusEmailTx.text = "E-mail: ${cusEmailTf.text}"
        cusDateTx.text = date
    }

    private fun initExecutor() {
        executorTx.text = user.abbreviatedExecutor
        exAddressTx.text = user.address
        exPATx.text = "Р/c: ${user.pa} в ${user.bank}"
        exBankAddressTx.text = user.bankAddress
        exBIKTx.text = "БИК: ${user.bik}"
        exPRNTx.text = "УНП: ${user.prn}"
        exPhoneTx.text = "Тел.: +${user.phone}"
        exEmailTx.text = "E-mail: ${user.email}"
        exFullNameTx.text = "__________________________________________/${user.abbreviatedExecutor}"
    }

    fun loadData(path: String?) {
        if (path != null)
            with(fromJSON<Contract>(path)) {
                this@CreateContract.path = path
                executorTx.text = exAbbreviatedCompanyName
                exAddressTx.text = exAddress
                exPATx.text = "Р/c: $exPA в $exBank"
                exBankAddressTx.text = exBankAddress
                exBIKTx.text = "БИК: $exBIK"
                exPRNTx.text = "УНП: $exPRN"
                exPhoneTx.text = "Тел.: $exPhone"
                exEmailTx.text = "E-mail: $exEmail"
                exFullNameTx.text = "__________________________________________/${exAbbreviatedCompanyName}"
                cusPRNTf.address = cusAddress!!
                cusPRNTf.prn = cusPRN?.toInt() ?: 0
                cusPRNTf.company = cusCompany
                cusPATf.pa = cusPA ?: ""
                cusPATf.bank = cusBank ?: ""
                cusPATf.bankAddress = cusBankAddress
                cusPATf.bik = cusBIK ?: ""
                dateP.editor.text = date
                cusFullNameTf.text = cusFullName
                footingTf.text = footing
                cusInPersonTf.text = cusInPerson
                cusPhoneTf.value = cusPhone
                cusEmailTf.text = cusEmail
            }
        else initExecutor()
        initContract()
    }

    class Contract(
        var date: String? = null, var footing: String = "", var exCompanyName: String = "",
        var exAbbreviatedCompanyName: String? = null,
        var exRegsCert: String? = null, var exAddress: String? = null, var exPA: String? = null, var exBank: String? = null,
        var exBankAddress: String? = null, var exBIK: String? = null, var exPRN: String? = null,
        var exPhone: String? = null, var exEmail: String? = null, var cusCompany: String = "",
        var cusFullName: String = "", var cusInPerson: String = "", var cusAddress: String? = null,
        var cusPA: String? = null, var cusBank: String? = null, var cusBankAddress: String = "", var cusBIK: String? = null,
        var cusPRN: String? = null, var cusPhone: String = "", var cusEmail: String = ""
    )
}
