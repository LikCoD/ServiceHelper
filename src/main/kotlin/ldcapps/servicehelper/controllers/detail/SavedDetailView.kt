package ldcapps.servicehelper.controllers.detail

import javafx.stage.Screen
import ldcapps.servicehelper.DetailND
import ldcapps.servicehelper.SavedDetails
import ldcapps.servicehelper.arrFromJSON
import tornadofx.*

class SavedDetailView : View() {

    override val root = form {
        val savedDetails = arrFromJSON<SavedDetails>(".saveddetails").asObservable()
        maxHeight = Screen.getPrimary().bounds.height - 170
        scrollpane {
            vbox(15) {
                savedDetails.forEach {
                    tableview(it.details.asObservable()) {
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
