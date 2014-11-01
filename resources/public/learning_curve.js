var result = [['Year', 'Correct Count', 'Wrong Count']];
$.ajax({
    type: 'GET',
    url: './diary-logs',
    datatype: 'json',
    success: function(json){
	$.each(json, function(i, item) {
	    result.push([new Date(item["date"]),
			 item["content"]["pos-cnt"],
			 item["content"]["neg-cnt"]]);})}});

function drawChart() {
    var data = google.visualization.arrayToDataTable(result);
    var options = {
	title: 'Learning Curve',
	dataType: 'Continuous'};
    var chart = new google.visualization.LineChart(document.getElementById('chart'));
    chart.draw(data, options);
}

google.load("visualization", "1", {packages:["corechart"]});
google.setOnLoadCallback(drawChart);
