package ldcapps.servicehelper.controllers.detail

import javafx.print.*
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.HBox
import javafx.scene.transform.Scale
import tornadofx.*

class PrintView(private var hb: HBox) : View() {
    override val root = form {
        vbox {
            val printerCb = combobox(values = Printer.getAllPrinters().toList()) {
                value = Printer.getDefaultPrinter()
            }
            val paperCb = combobox(
                values = listOf(
                    Paper.A0,
                    Paper.A1,
                    Paper.A2,
                    Paper.A3,
                    Paper.A4,
                    Paper.A5,
                    Paper.A6
                ).asObservable()
            ) {
                value = Paper.A4
            }
            val orientationCb = combobox(values = PageOrientation::class.java.enumConstants.toList()) {
                value = PageOrientation.PORTRAIT
            }
            val marginCb = combobox(values = Printer.MarginType::class.java.enumConstants.toList()) {
                value = Printer.MarginType.EQUAL_OPPOSITES
            }
            val sideCb = combobox(values = PrintSides::class.java.enumConstants.toList()) {
                value = PrintSides.DUPLEX
            }
            val copiesSn = spinner<Int>(false, enableScroll = true) {
                valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, Int.MAX_VALUE, 1)
            }
            button("Печать") {
                action {
                    close()
                    val job = PrinterJob.createPrinterJob(printerCb.value)
                    val pageLayout = job.printer.createPageLayout(
                        paperCb.value,
                        orientationCb.value,
                        marginCb.value
                    )
                    job.jobSettings.printSides = sideCb.value
                    job.jobSettings.copies = copiesSn.value
                    for (i in 0 until hb.children.size) {
                        hb.children[0].style {
                            borderColor.elements.clear()
                        }
                        val scale =
                            if (pageLayout.printableWidth / hb.children[0].boundsInParent.width < pageLayout.printableHeight / hb.children[0].boundsInParent.height)
                                pageLayout.printableWidth / hb.children[0].boundsInParent.width else pageLayout.printableHeight / hb.children[0].boundsInParent.height
                        hb.children[0].transforms.add(Scale(scale, scale))
                        job.printPage(pageLayout, hb.children[0])
                        hb.children.remove(hb.children[0])
                    }
                    job.endJob()
                }
            }
        }
    }
}
