var sMon = new Array(12);
	sMon[0] = "January"
	sMon[1] = "February"
	sMon[2] = "March"
	sMon[3] = "April"
	sMon[4] = "May"
	sMon[5] = "June"
	sMon[6] = "July"
	sMon[7] = "August"
	sMon[8] = "September"
	sMon[9] = "October"
	sMon[10] = "November"
	sMon[11] = "December"
var sRootPath = "/ArchiveWeb";
function calendar(t) {
		var sPath = sRootPath+"/common/include/Calendar1.htm";
		strFeatures = "dialogWidth=206px;dialogHeight=180px;center:yes;edge:raised;scroll:no;status:no;unadorned:yes;help:no";
		st = t.value;
		sDate = showModalDialog(sPath,st,strFeatures);
		t.value = formatDate(sDate, 0);

}
function calendarEx(t,commonPath){
    if (commonPath==null||commonPath==""){
      commonPath = sRootPath+"/common";
    }
    var sPath = commonPath+"/include/Calendar1.htm";
    strFeatures = "dialogWidth=206px;dialogHeight=180px;center:yes;edge:raised;scroll:no;status:no;unadorned:yes;help:no";
		st = t.value;
		sDate = showModalDialog(sPath,st,strFeatures);
		t.value = formatDate(sDate, 0);
}
function checkDate(t) {
	dDate = new Date(t.value);
	if (dDate == "NaN") {t.value = ""; return;}

	iYear = dDate.getFullYear()

	if ((iYear > 1899)&&(iYear < 1950)) {

		sYear = "" + iYear + ""
		if (t.value.indexOf(sYear,1) == -1) {
			iYear += 100
			sDate = (dDate.getMonth() + 1) + "/" + dDate.getDate() + "/" + iYear
			dDate = new Date(sDate)
		}
	}



	t.value = formatDate(dDate);
}

function formatDate(sDate) {
	var sScrap = "";
	var dScrap = new Date(sDate);
	if (dScrap == "NaN") return sScrap;

	iDay = dScrap.getDate();
	iMon = dScrap.getMonth();
	iYea = dScrap.getFullYear();

	sScrap = iYea + "-" + (iMon + 1) + "-" + iDay ;
	return sScrap;
}

function isDate(op, formatString){
		formatString = formatString || "ymd";
		var m, year, month, day;
		switch(formatString){
			case "ymd" :
				m = op.match(new RegExp("^((\\d{4})|(\\d{2}))([-./])(\\d{1,2})\\4(\\d{1,2})$"));
				if(m == null ) return false;
				day = m[6];
				month = m[5]--;
				year =  (m[2].length == 4) ? m[2] : GetFullYear(parseInt(m[3], 10));
				break;
			case "dmy" :
				m = op.match(new RegExp("^(\\d{1,2})([-./])(\\d{1,2})\\2((\\d{4})|(\\d{2}))$"));
				if(m == null ) return false;
				day = m[1];
				month = m[3]--;
				year = (m[5].length == 4) ? m[5] : GetFullYear(parseInt(m[6], 10));
				break;
			default :
				break;
		}
		if(!parseInt(month)) return false;
		month = month==12 ?0:month;
		var date = new Date(year, month, day);
        return (typeof(date) == "object" && year == date.getFullYear() && month == date.getMonth() && day == date.getDate());
		
	}
	function GetFullYear(y)
	{
	      return ((y<30 ? "20" : "19") + y)|0;
     }

