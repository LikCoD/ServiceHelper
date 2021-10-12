package ldcapps.servicehelper

enum class FXMLInfo(val path: String, val minWidth: Double? = null, val minHeight: Double? = null) {
    Main("Main"),
    ToolSelector("Tools/Main"),
    Print("Print"),
    CreateAct("CreateAct"),
    Blank("Blank"),
    Login("Login"),
    CarData("CarData", 275.0, 335.0),
    OO("OO", 1100.0, 700.0),
    OOCollapsed("Tools/Add car", 600.0, 360.0),

    TOOL_ADD_CAR("Tools/Add car", 650.0, 350.0),
    TOOL_CREATE_CONTRACT("Tools/Create contract", 1000.0, 350.0),
    TOOL_REDACT_DB("Tools/Redact DB", 800.0, 350.0),
    TOOL_GET_REPORT("Tools/Get report", 800.0, 350.0),
    TOOL_SING_UP("Tools/Signup", 750.0, 730.0),
}