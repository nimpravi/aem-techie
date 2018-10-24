
CQ.Ext.StoreMgr.register(new CQ.Ext.data.SimpleStore({
    storeId: "clickstreamstore",
    data: [],
    fields: ["key", "value"],
    id: 0
}));


CQ_Analytics.ClickstreamcloudMgr.addListener("storesloaded", function(e) {
    var data = new Array();
    var dataMgrs = {
        profile: CQ_Analytics.ProfileDataMgr,
        pagedata: CQ_Analytics.PageDataMgr,
        surferinfo: CQ_Analytics.SurferInfoMgr,
        eventdata: CQ_Analytics.EventDataMgr
    };
    for(var mgr in dataMgrs) {
        if( dataMgrs[mgr] ) {
            var profileNames = dataMgrs[mgr].getPropertyNames();
            var title = CQ_Analytics.ClickstreamcloudMgr.getUIConfig(mgr).title;
            for(var i=0; i < profileNames.length; i++) {
                if (!CQ.shared.XSS.KEY_REGEXP.test(profileNames[i])) {
                    data.push([mgr + "." + profileNames[i], mgr + "." + profileNames[i] ]);
                }
            }
        }
    }
    CQ.Ext.StoreMgr.lookup("clickstreamstore").loadData(data);
});
