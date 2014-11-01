var result = [];
$.ajax({
    type: 'GET',
    url: './diary-logs',
    async: false,
    datatype: 'json',
    success: function(json){
	$.each(json, function(i, item) {
	    result.push([new Date(item["date"]),
			 item["content"]["pos-cnt"],
			 item["content"]["neg-cnt"]]);})}});

function drawChart() {
    var data = new google.visualization.DataTable();
    data.addColumn('datetime', 'Year');
    data.addColumn('number', 'Correct Count');
    data.addColumn('number', 'Wrong Count');
    data.addRows(result);
    var options = {title: 'Learning Curve',
		   isStacked: true};
    var chart = new google.visualization.ColumnChart(document.getElementById('chart'));
    chart.draw(data, options);
}

google.load("visualization", "1.1", {packages:["corechart"]});
google.setOnLoadCallback(drawChart);
