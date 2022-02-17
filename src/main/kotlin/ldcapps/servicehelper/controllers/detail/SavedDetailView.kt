package ldcapps.servicehelper.controllers.detail

import javafx.stage.Screen
import ldcapps.servicehelper.DetailND
import ldcapps.servicehelper.arrFromJSON
import ldcapps.servicehelper.savedDetails
import tornadofx.*

class SavedDetailView : View() {

    override val root = form {
        savedDetails = arrFromJSON<Array<DetailND>>(".saveddetails").asObservable()
        maxHeight = Screen.getPrimary().bounds.height - 170
        scrollpane {
            vbox(15) {
                savedDetails.forEach {
                    tableview(it.toMutableList().asObservable()) {
                        readonlyColumn("Дата", DetailND::date)
                        readonlyColumn("Авто", DetailND::car)
                        readonlyColumn("Деталь", DetailND::detail)
                        readonlyColumn("Цена", DetailND::price)
                        readonlyColumn("Заказчик", DetailND::customer)
                    }
                }
            }
        }
    }
}
