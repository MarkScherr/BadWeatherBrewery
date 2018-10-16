var beerType = " ";
var id = 'tVmF6TeP1HxPXbx9PSZC';
var code = '42o69dTNE1DSXt7_wn11xg';
var coordinateArray = [];
$(document).ready(function () {
	loadBeers();
	
	$('#submitButton').on('click', function() {
		getBeerInformation();
	});
	$(function() {
		$('#datepicker').datepicker({ dateFormat: 'mm-dd-yy' }).val()
	});

	$(function() {
		$('#toDatepicker').datepicker({ dateFormat: 'mm-dd-yy' }).val()
	});
	
});

function setUpMap(quantityArray) {
	$('#map').empty();
	const platform = new H.service.Platform({
	  'app_id': id,
	  'app_code': code
	});
	// Obtain the default map types from the platform object:
	var defaultLayers = platform.createDefaultLayers();

	// Instantiate (and display) a map object:
	var map = new H.Map(
	  document.getElementById('map'),
	  defaultLayers.normal.map,
	  {
		zoom: 10,
		center: { lat: 44.9, lng: -93.1 }
	  });
	var behavior = new H.mapevents.Behavior(new H.mapevents.MapEvents(map));
	var locations = [];
	for(var i = 0; i < coordinateArray.length ; i++) {
		var latitude = 0;
		var longitude = 0;
		for(var j = 0; j < coordinateArray[i].length ; j++) {
			if(j == 0) {
				latitude = coordinateArray[i][j];
			} else {
				longitude = coordinateArray[i][j];
			}
		}
/* 		var thisMarker = new H.map.Marker({lat:latitude, lng:longitude});
		map.addObject(thisMarker); */
		    var markupTemplate = '<svg xmlns="http://www.w3.org/2000/svg" width="28px" height="36px"><path d="M 19 31 C 19 32.7 16.3 34 13 34 C 9.7 34 7 32.7 7 31 C 7 29.3 9.7 28 13 28 C 16.3 28 19 29.3 19 31 Z" fill="#000" fill-opacity=".2"/><path d="M 13 0 C 9.5 0 6.3 1.3 3.8 3.8 C 1.4 7.8 0 9.4 0 12.8 C 0 16.3 1.4 19.5 3.8 21.9 L 13 31 L 22.2 21.9 C 24.6 19.5 25.9 16.3 25.9 12.8 C 25.9 9.4 24.6 6.1 22.1 3.8 C 19.7 1.3 16.5 0 13 0 Z" fill="#fff"/><path d="M 13 2.2 C 6 2.2 2.3 7.2 2.1 12.8 C 2.1 16.1 3.1 18.4 5.2 20.5 L 13 28.2 L 20.8 20.5 C 22.9 18.4 23.8 16.2 23.8 12.8 C 23.6 7.07 20 2.2 13 2.2 Z" fill="#18d"/><text x="13" y="19" font-size="12pt" font-weight="bold" text-anchor="middle" fill="#fff">${text}</text></svg>',

        // Set your text here.
        quantity = quantityArray[i];
		if(quantity >= 500) {		
			var myMarker = "img/BWMapIconOver500.png";
		} else if ( quantity >= 250) {
			var myMarker = "img/BWMapIconOver250.png";			
		} else if ( quantity >= 100) {
			var myMarker = "img/BWMapIconOver100.png";			
		} else {
			var myMarker = "img/BWMapIcon.png";			
		}
		var markup = markupTemplate.replace('${text}', quantity),
			icon = new H.map.Icon(myMarker),
			marker = new H.map.Marker({
				lat: latitude,
				lng: longitude
			}, {
				icon: icon
			});

		map.addObject(marker);

	}
		

}
function loadBeers() {
	  
	  $.ajax({
        type: "GET",
        url: "http://localhost:8080/bw/beers",
        
		success: function (data) {
			console.log(data);
			$.each(data, function(index, product){
				var beerName = product;
				console.log(product);
				$('.dropdown-menu').append('<li><a href="#">' + product + '</a></li>');
				
			});
			setBeerClick();
        },
        error: function (e) {
			alert("this didn't work");

        }
    });
}

/* $('#datepicker').datepicker({
	onSelect: function() {
		var date = $('#datepicker').datepicker({ dateFormat: 'dd-mm-yy' }).val();
		from = date;
	}
});

$('#toDatepicker').datepicker({
	onSelect: function() {
		to = ' ';
		var date = $('#toDatepicker').datepicker({ dateFormat: 'dd-mm-yy' }).val();
		if(date == '') {
			alert('blank');
		} else {
			to = date;
		}
		
	}
});
 */
function setBeerClick() {
	$("#beerMenu li").on( "click", function() {
		$('#beerSelected').empty();
		var value = $(this).text();
		beerType = value;
		$('#beerSelected').append('<h4>' + value + '</h4>');
	});
}
function validateDate(date) {
	if(date == "") {
		return " ";
	} else {
		return date;
	}
}
function getBeerInformation(){
	var productTypeArray = [];
	var cityArray = [];
	var storeArray = [];
	var quantityArray = [];
	var fromDate = $('#datepicker').datepicker({ dateFormat: 'dd-mm-yy' }).val();
	fromDate = validateDate(fromDate);
	var toDate = $('#toDatepicker').datepicker({ dateFormat: 'dd-mm-yy' }).val();
	toDate = validateDate(toDate);
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/bw/beerInformation/" + beerType + "/" + fromDate + "/" + toDate + "/",

		success: function (data) {
			$.each(data, function(index, item){
				productTypeArray.push(item.productType);
				cityArray.push(item.city);
				storeArray.push(item.store);
				quantityArray.push(item.quantity);
			});

			console.log(cityArray);
			findUniqueCitiesWithQuantities(cityArray, quantityArray);
			findStoresPurchasesByProductType(productTypeArray, storeArray, quantityArray);
		},
		error: function (e) {
			alert("this didn't work");

		}
	});
	
}

function findUniqueCitiesWithQuantities(cityArray, quantityArray) {
	var uniqueCity = [];
	var uniqueQuantity = [];
	coordinateArray = [];
	$('#salesPerCityDiv').empty()
	for(var i=0; i < cityArray.length ; i++) {
		if(uniqueCity.indexOf(cityArray[i]) > -1) {
			uniqueQuantity[uniqueCity.indexOf(cityArray[i])] =
							(uniqueQuantity[uniqueCity.indexOf(cityArray[i])] + quantityArray[i]);
		} else {
			uniqueCity.push(cityArray[i]);
			
			getCoordinates(cityArray[i]);
			uniqueQuantity.push(quantityArray[i]);
		}
	}
	console.log(uniqueCity);
	console.log(uniqueQuantity);
	for(var i = 0 ; i < uniqueCity.length ; i++) {
		$('#salesPerCityDiv').append('<h4>City: ' + uniqueCity[i]+ '</h4><h4>Quanity: ' + uniqueQuantity[i]+ '</h4><br>');
	}
	setTimeout(function() {
		setUpMap(uniqueQuantity);
		console.log(coordinateArray);
	}, 500);
	
}

function findStoresPurchasesByProductType(productTypeArray, storeArray, quantityArray) {
	var storeUniqueArray = [];
	var productTypeUniqueToStoreArray = [];
	var quantitiesUniqueToStoreAndItem = [];
	$('#salesPerStoreDiv').empty()
	for(var i=0; i < productTypeArray.length ; i++) {
		var isDifferentProductButNotUniqueStore = false;
		if(storeUniqueArray.indexOf(storeArray[i]) > -1) {
			var storeName = storeUniqueArray[storeUniqueArray.indexOf(storeArray[i])];
			for(var j = 0; j< storeUniqueArray.length ; j++) {
				if(storeUniqueArray[j] == storeName && productTypeUniqueToStoreArray[j] == productTypeArray[i]) {
					quantitiesUniqueToStoreAndItem[j] += quantityArray[i];
					isDifferentProductButNotUniqueStore = true;
					break;
				}
				
			}
			if(!isDifferentProductButNotUniqueStore) {
				storeUniqueArray.push(storeArray[i]);
				productTypeUniqueToStoreArray.push(productTypeArray[i]);
				quantitiesUniqueToStoreAndItem.push(quantityArray[i]);
				
			}
		} else {
			storeUniqueArray.push(storeArray[i]);
			productTypeUniqueToStoreArray.push(productTypeArray[i]);
			quantitiesUniqueToStoreAndItem.push(quantityArray[i]);
		}
		
		
	}
	console.log(storeUniqueArray);
	console.log(productTypeUniqueToStoreArray);	
	console.log(quantitiesUniqueToStoreAndItem);
	
	for(var i = 0 ; i < storeUniqueArray.length ; i++){
		if(i > 0 && storeUniqueArray[i] == storeUniqueArray[i-1]) {
			$('#salesPerStoreDiv').append('<h4>Product: ' + 
					productTypeUniqueToStoreArray[i] + ', Quantity: ' + quantitiesUniqueToStoreAndItem[i] + '</h4><br>');
		} else {
			$('#salesPerStoreDiv').append('<h4>Store: ' + storeUniqueArray[i]+ '</h4><h4>Product: ' + 
					productTypeUniqueToStoreArray[i] + ', Quantity: ' + quantitiesUniqueToStoreAndItem[i] + '</h4><br>');
		}
	}

}

function getCoordinates(city){
	var currentCoordinates = [];
	$.ajax({
		type: "GET",
		url: "https://geocoder.api.here.com/6.2/geocode.json?app_id=" + id + "&app_code=" +
				code + "&searchtext=" + city + "+MN",
		success: function (data) {
			$.each(data.Response.View, function(index, View){					
				$.each(View.Result, function(index, Result){	
					$.each(Result.Location.NavigationPosition, function(index, NavigationPosition){
						currentCoordinates.push(NavigationPosition.Latitude);
						currentCoordinates.push(NavigationPosition.Longitude);
						
					});
				});
			});
		},
		error: function (e) {
			alert("this didn't work");

		}
	});
	coordinateArray.push(currentCoordinates);
	
	
}