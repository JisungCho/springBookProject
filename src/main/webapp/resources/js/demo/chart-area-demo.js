
// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

//오늘
function getToday(){
	var nowDate = new Date();
	var nowYear = nowDate.getFullYear();
	var nowMonth = nowDate.getMonth() + 1;
	var nowDay = nowDate.getDate();
	
	if(nowMonth < 10){ nowMonth = "0" + nowMonth; }
	if(nowDay < 10) { nowDay = "0" + nowDay; }
	return nowYear + "-" + nowMonth + "-" + nowDay;

};

//어제
function getYesterday(){
	var nowDate = new Date();
	var yesterDate = nowDate.getTime() - (1 * 24 * 60 * 60 * 1000);
	nowDate.setTime(yesterDate);
	var yesterYear = nowDate.getFullYear();
	var yesterMonth = nowDate.getMonth() + 1;
	var yesterDay = nowDate.getDate();
	
	if(yesterMonth < 10){ yesterMonth = "0" + yesterMonth; }
	if(yesterDay < 10) { yesterDay = "0" + yesterDay; }
	
	return yesterYear + "-" + yesterMonth + "-" + yesterDay;
};


//그제
function getTwoDaysAgo(){
	var nowDate = new Date();
	var yesterDate = nowDate.getTime() - (2 * 24 * 60 * 60 * 1000);
	nowDate.setTime(yesterDate);
	var yesterYear = nowDate.getFullYear();
	var yesterMonth = nowDate.getMonth() + 1;
	var yesterDay = nowDate.getDate();
	
	if(yesterMonth < 10){ yesterMonth = "0" + yesterMonth; }
	if(yesterDay < 10) { yesterDay = "0" + yesterDay; }
	
	return yesterYear + "-" + yesterMonth + "-" + yesterDay;
};

// Area Chart Example
var ctx = document.getElementById("myAreaChart");
var myLineChart = new Chart(ctx, {
  type: 'line',
  data: {
    labels: [getTwoDaysAgo(),getYesterday(),getToday()],
    datasets: [{
      label: "방문자수",
      lineTension: 0.3,
      backgroundColor: "rgba(78, 115, 223, 0.05)",
      borderColor: "rgba(78, 115, 223, 1)",
      pointRadius: 3,
      pointBackgroundColor: "rgba(78, 115, 223, 1)",
      pointBorderColor: "rgba(78, 115, 223, 1)",
      pointHoverRadius: 3,
      pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
      pointHoverBorderColor: "rgba(78, 115, 223, 1)",
      pointHitRadius: 10,
      pointBorderWidth: 2,
	  //데이터 가져오기 (그제 , 오늘 , 내일)
      data: [$("#twoDaysAgo").val(),$("#yesterday").val(),$("#today").val()],
    }],
  },
  options: {
    maintainAspectRatio: false,
    layout: {
      padding: {
        left: 10,
        right: 25,
        top: 25,
        bottom: 0
      }
    },
    scales: {
      xAxes: [{
        time: {
          unit: 'date'
        },
        gridLines: {
          display: false,
          drawBorder: false
        },
        ticks: {
          maxTicksLimit: 3
        }
      }],
      yAxes: [{
        ticks: {
		  beginAtZero: true,
          maxTicksLimit: 5,
          padding: 5,
          callback: function(value, index, values) {
             	return value+ "명";
          }
        },
        gridLines: {
          color: "rgb(234, 236, 244)",
          zeroLineColor: "rgb(234, 236, 244)",
          drawBorder: false,
          borderDash: [2],
          zeroLineBorderDash: [2]
        }
      }],
    },
    legend: {
      display: false
    },
    tooltips: {
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      titleMarginBottom: 10,
      titleFontColor: '#6e707e',
      titleFontSize: 14,
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      intersect: false,
      mode: 'index',
      caretPadding: 10,
      callback: function(value, index, values) {
             	return value+ "명";
      }
    }
  }
});
