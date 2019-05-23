  /**  进行时间比较
   * @param a_dInput1/a_dInput2 String 格式'****-**-**'
   * @return l_sReturn 1-大于,0－等于,-1－小于,-2－出错
   */
  function dateCompare(a_sInput1,a_sInput2)
  {
    var l_sReturn;
    var l_dInput1,l_dInput2;
    var l_sTemp;

    if(a_sInput1 == "")
      return -2;
    if(a_sInput2 == ""){
      l_dInput2 = new Date();
    }else{
      a_sInput2 = a_sInput2.split("-");
      l_dInput2 = new Date(a_sInput2[0],a_sInput2[1],a_sInput2[2]);
    }

    a_sInput1 = a_sInput1.split("-");
    l_dInput1 = new Date(a_sInput1[0],parseInt(a_sInput1[1])-1,a_sInput1[2]);
    l_dInput1.setHours(23);
    /*
    alert(l_dInput1.toString());
    alert(l_dInput2.toString());
    */
    if(l_dInput1 == l_dInput2)
      return 0;
    if(l_dInput1 > l_dInput2)
      return 1;
    if(l_dInput1 < l_dInput2)
      return -1;
  }