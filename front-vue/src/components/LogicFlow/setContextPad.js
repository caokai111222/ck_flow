const setContextPad = (lf) => {
  const userConfig = {
    type: "bpmn:userTask",
    label: "用户任务",
    icon:
      "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAAAXNSR0IArs4c6QAAAp1JREFUWEftlj9oE2EYxp/3IkSckotOpqDgl5ZWcLBgxcVBdBL8gw66iCg2lxrBzcl0qpPS1lyQKm5uiuBUHBztoAjVVtOLRDA46Z06VAvmXkkwQWLS3PudQwn54KY8z/v8eO7uvRA2+KENzoc+YNg71PsNxm6VYxStnibwIQCnAF4kptcMv+RWjSlk1VqYFkM1mLCLx5jpJoh2doD4zoQpL61u6EJqA5oF5zwY9wIFE3JuWk0G0raItAATd8r7uPprQRRoYMQdV8siD6C3ZkzbeQ5gTBLGwGPPUsclnppW3GCs8HaHwZvK0iAAq5sj37Z9ujS6KvGKARP5lXNMdF8S0tT6fNidSD2VeMWAcfv9OMEvSEKaWo2XRQwYm3UOGhE80wEk5pNfMqlHEq8YMFEobmc2KpKQhpZR3eNZQ4sSrxiwNty0Sw8BPiEJAujDlp/R4crVgR8Snxagzm1m8FnPSj2QwGmtmUaAaTtzAC4ECWTCNd3PnVaDDahEoZRl5ul1ITVWy9/zQgHWBsULpQPw+QwIw2CM1IcTlsBYZgN3v6bVqyAtd9KEBgwTHsTb+4DmnJPEGgZgIAlCst4KowIfFUTx0b2otHZmo11xg+ZtZz8iGAPjCFC/gpx5EOZRxYI7oWr/hAKfQIBb7XeDDOMKMx1tthQ4okXIqBDxE4I//dkaKnYb0xXQzBdzoEgW4Hi3YbLfyQNXZ9zMYG4937qApu28Af6sDlm6RL3kWmq3eM3EbecFAXslSbpaBl56lhpt52/bYNxemSHQZd1AHR+DZz0rlW31tgfMOw4RdukE6XqYUfIySgUCNG2HdYPC+FxL/VNY2wb7gB1q7qEG68vZuB7meRJ72Z9st7S7fknEQf/Z0AcMW2i/wbAN/gbA1dYpL5kz5gAAAABJRU5ErkJggg==",
    properties: {
      actived: true
    }
  };
  const serviceConfig = {
    type: "bpmn:serviceTask",
    label: "系统任务",
    icon:
      "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAAAXNSR0IArs4c6QAAAz9JREFUWEfVmEtoE1EUhv8zqaBgwUwi+K5CJqkoumhBxY11IVTBhaBUUNCFtplQRRSrK+vKVrTgYyZUF7qRigV3dqFg3YhdtAsLapIJKK2vRToRqgtpM0cmtTVJE3IzyWKcXTL/+c83c+/cc+4luPwil/OhKsCVWjJgEVqZM75iD8rk+co08/xHePMnpy/CMaANlyE2RBJbNLvJKaRjQF/UuMaMSyKAROiZCiuXRbSFGseAsp4cALhNLCk9NtXAUTFtvqoKQOMNgJ2CSUdMVdklqM2TlQRc1ze57NfS38eJM2uY+GlabRzPjZR14xuAVYJJv5uqsjpX69Vj24jpECRpAoQRs0N5X8yrKKCsJdeDeBDAjvkgBl5JzHdm6+itlEEfAQcF4eZkRENShs4xZbZaRJ0E7MmJfwemVjMSmBSag7KWvAXiMxUBVCtmum1GAmfLAvq1ZItF/LLafE7iJaa9qUhgODd20RDL0eQzMO93kqDqGKIhMxw4UBJQ1uKnQNK9yhPxFwYZEjhmEc0SEAIoBOYNFXuxddqMhO7Pxy28wfr+uH9JRnoNIFiJKTGfnIoEHxaL8WqJi0TUW4kfgMSMx9o93R5KZb+t+WBZNw4DeFKJmUgJ8+vJJgs8WokvE46lw8qjfEAt3g2SrogaSaDmlBoYE9GviH7YKHHdRxFtVsPWVTMS6s4DtH94dWOUgKZyRszclY4Er5fT5d73aYkTTPSgfAyPm2pw+6I5aP/h743VW8ulfhCVrptEE2Y40FA+0WKFrCc+A7S2ZCzzgPTTak91NU4XBVyYj1r8PEi6UdSI8MIMK/ucAHp1Y7iggvyzYeuCGQndLPQtWYtLmTHhbjqsdDoB9OmJKIM6CmPtMppWlZZinv8noOzWIXb9R+LqZUZ2+0Lt+lLn+mbBXoNc3W4tVBE3N6zZeuz2ln9uqF28afoLWHLbyR6Kwco2tlsqqcfMSHqI2pithqq3nXZigY27abeQgpBpU1XkXG1VG3eRpKJVJ9sgA2NpVWkW8RVut8qZybphD7O9jxG5Bk1VOSIirBmgTzd6GOgSSUpA75SqCB3V1QzQ9QeY9pO6+ghYZGhroXF8gFmL5CIefwB0scE4DheJlAAAAABJRU5ErkJggg==",
    cls: "import_icon"
  };
  const endConfig = {
    type: "bpmn:endEvent",
    label: "结束",
    icon:
      "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAAH6ji2bAAAABGdBTUEAALGPC/xhBQAAA1BJREFUOBFtVE1IVUEYPXOf+tq40Y3vPcmFIdSjIorWoRG0ERWUgnb5FwVhYQSl72oUoZAboxKNFtWiwKRN0M+jpfSzqJAQclHo001tKkjl3emc8V69igP3znzfnO/M9zcDcKT67azmjYWTwl9Vn7Vumeqzj1DVb6cleQY4oAVnIOPb+mKAGxQmKI5CWNJ2aLPatxWa3aB9K7/fB+/Z0jUF6TmMlFLQqrkECWQzOZxYGjTlOl8eeKaIY5yHnFn486xBustDjWT6dG7pmjHOJd+33t0iitTPkK6tEvjxq4h2MozQ6WFSX/LkDUGfFwfhEZj1Auz/U4pyAi5Sznd7uKzznXeVHlI/Aywmk6j7fsUsEuCGADrWARXXwjxWQsUbIupDHJI7kF5dRktg0eN81IbiZXiTESic50iwS+t1oJgL83jAiBupLDCQqwziaWSoAFSeIR3P5Xv5az00wyIn35QRYTwdSYbz8pH8fxUUAtxnFvYmEmgI0wYXUXcCCSpeEVpXlsRhBnCEATxWylL9+EKCAYhe1NGstUa6356kS9NVvt3DU2fd+Wtbm/+lSbylJqsqkSm9CRhvoJVlvKPvF1RKY/FcPn5j4UfIMLn8D4UYb54BNsilTDXKnF4CfTobA0FpoW/LSp306wkXM+XaOJhZaFkcNM82ASNAWMrhrUbRfmyeI1FvRBTpN06WKxa9BK0o2E4Pd3zfBBEwPsv9sQBnmLVbLEIZ/Xe9LYwJu/Er17W6HYVBc7vmuk0xUQ+pqxdom5Fnp55SiytXLPYoMXNM4u4SNSCFWnrVIzKG3EGyMXo6n/BQOe+bX3FClY4PwydVhthOZ9NnS+ntiLh0fxtlUJHAuGaFoVmttpVMeum0p3WEXbcll94l1wM/gZ0Ccczop77VvN2I7TlsZCsuXf1WHvWEhjO8DPtyOVg2/mvK9QqboEth+7pD6NUQC1HN/TwvydGBARi9MZSzLE4b8Ru3XhX2PBxf8E1er2A6516o0w4sIA+lwURhAON82Kwe2iDAC1Watq4XHaGQ7skLcFOtI5lDxuM2gZe6WFIotPAhbaeYlU4to5cuarF1QrcZ/lwrLaCJl66JBocYZnrNlvm2+MBCTmUymPrYZVbjdlr/BxlMjmNmNI3SAAAAAElFTkSuQmCC"
  };
  const deleteConfig = {
    icon:
      "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAAAXNSR0IArs4c6QAAAUpJREFUWEftVzuywjAMlKgDPV2g4RYPuAnfDs7w4AzQ8b0Jn1vQQDp6SI0YFwFPSMC2EiYZnDLZldYrybERGI/XLs4Aofc2BMHcXV37pmnQlOh1SwMgmijxEYfu4jJVwoZARgKPXeevQLjTSXhDqlcX/l6HI7APgaeOM0LCulIAhDIA1JSwT9ABCM4qHELaVZb+6FUg4L9KgLQxBDTOqUDVEqdsYWSJU85pHN5oio2zGRDzK/DUchpYwI3BorUpdKNmZe1vo4ixDlqBkl2JOCiCaNfuDUFun0QEustrogPldYoU6P9dgWKQ5CoGkxj3XsZ+xcG4JOEdIKo9rEBRLuugfKIOb1mcPuJwwzqUf3U6jW4FCps5LnC4tsRc962D1sGgBziTyOHaHsxUD2b+wMq5RH3lwJopgZm/dnLc+sRN5Nr5KQnne64F3gEyDFZHJCakvQAAAABJRU5ErkJggg==",
    callback: (data) => {
      lf.deleteElement(data.id);
      lf.extension.contextPad.hideContextMenu();
    }
  };
  lf.extension.contextPad.setContextMenuItems([deleteConfig]);
  lf.extension.contextPad.setContextMenuByType("bpmn:startEvent", [
    userConfig,
    serviceConfig,
  ]);
  lf.extension.contextPad.setContextMenuByType("bpmn:userTask", [
    userConfig,
    serviceConfig,
    endConfig
  ]);
  lf.extension.contextPad.setContextMenuByType("bpmn:serviceTask", [
    userConfig,
    serviceConfig,
    endConfig
  ]);
};

export default setContextPad;
