<%@ page import="java.util.Date"%>
<%@ page import="java.security.SecureRandom,javax.crypto.*,javax.crypto.spec.*,java.nio.charset.Charset,java.nio.charset.spi.CharsetProvider" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<title>OMX RESTful service client</title>
<script src="js/jquery-1.8.3.min.js" type="text/javascript"></script>
<script src="js/documentOverlay.js" type="text/javascript"></script>
<script src="js/md5.js" type="text/javascript"></script>
<script>
$(document).ready(function(){   
	documentOverlay.show();
	$.ajax(document.URL, {
		  statusCode: {
		    404: function() {
		      $("#result").html("not connected");
		    },
		    200: function() {
		      $("#result").html("connected to "+document.URL+"rest/omxdata/");
		    }
		  }
		});
	
    
    $("select#tradables").change(function(){
    	var selected = $("select#tradables").val();
    	if(selected>0){ //selected real tradable
    		$(".data").show();
    		
    	
    	    $.ajax({
                url: document.URL+"rest/omxdata",
                global: false,
                type: "GET",
                data: ({"id":selected}),
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                scriptCharset: "utf-8",
                success: function(tradable){
                     
                	var values = tradable.entity.id+" "+
                	tradable.entity.bestBid+" "+
                	tradable.entity.bestAsk+" "+
                	tradable.entity.contracts+" "+
                	tradable.entity.highestTradedPrice+" "+
                	tradable.entity.lowestTradedPrice+" "+
                	tradable.entity.closingPrice+" "+
                	tradable.entity.startTradingDate+" "+
                	tradable.entity.stopTradingDate+" "+
                	tradable.entity.startDeliveryDate+" "+
                	tradable.entity.stopDeliveryDate;
                	
                	 hmac = hex_hmac_md5("qnscAdgRlkIhAUPY44oiexBKtQbGY0orf7OV1I50", values);
                	
                	 
                	 if(hmac==tradable.entity.mac){ //md5 hash matched
                	 
                      $.each(tradable.entity,function(name,value){
                    	  
                    	  var isDate = $("#"+name).hasClass("date");	
                    	  $("#"+name).html(handleValues(value,isDate));
         	  
                      });
                	 }else{
                		 alert("Server message consistency failed: Hashes don't match");
                	 }
                }
          }); 	
    	}else{
    		$(".data").hide();
    	}
    });
     
    fetchTradableIDs();
    
});

function handleValues(value,isDate){
	if(value && $.trim(value).length>0){
		if(isDate){
			dateArray = $.trim(value).split(new RegExp("([0-9]{4})([0-9]{2})([0-9]{2})"));
			dateArray = dateArray.filter(function(element){return (element.length>0);});
			value = dateArray.join("-");
		}
		return value;
	}
	else return "-";
}

function fetchTradableIDs(){
    //sync ajax call to WebService
    
    $.ajax({
              url: document.URL+"rest/omxdata",
              global: false,
              type: "GET",
              
              dataType: "json",
              contentType: "application/json;charset=utf-8",
              scriptCharset: "utf-8",
              success: function(data){
            	    documentOverlay.hide();
                    $("select#tradables").append("<option value='-1'></option>");
                    $.each(data.entity,function(index,value){
                    	$("select#tradables").append("<option value=\""+value+"\">" + value + "</option>");                               	
                    });
                    
                    
              },
              statusCode: {
                  401:function() { alert("Authorization is unsuccessful!\nPlease try again"); }
                }
        });

}
</script>
<style>
table.data label {
    font-family: sans-serif;
    font-size: smaller;
    
}
table.data span {
    font-family: sans-serif;
    font-size: smaller;
    font-weight: bold;
}

table.data td{
    padding: 1px 10px;
}


</style>
</head>
<body>
<h2>OMX RESTful service client</h2>
Tradable&nbsp;<select id="tradables"></select>
<hr/>
<div id="container"></div>
<table class="data" style="display:none">
    <tr>
    	<td><label>Best Bid: </label></td><td><span id="bestBid"></span></td>
    	<td><label>Best Ask: </label></td><td><span id="bestAsk"></span></td>
	</tr>
    <tr>    	
    	<td><label>Contracts: </label></td><td><span id="contracts"></span></td>
    	<td><label>Highest Traded Price: </label></td><td><span id="highestTradedPrice"></span></td>
    </tr>
    <tr>
    	<td><label>Lowest Traded Price: </label></td><td><span id="lowestTradedPrice"></span></td>
    	<td><label>Closing price: </label></td><td><span id="closingPrice"></span></td>
    </tr>
    <tr>
        <td><label>Start Delivery Date: </label></td><td><span id="startDeliveryDate" class="date"></span></td>
    	<td><label>Stop Delivery Date: </label></td><td><span id="stopDeliveryDate" class="date"></span></td>
    </tr>
    <tr>    	
    	<td><label>Start Trading Date: </label></td><td><span id="startTradingDate" class="date"></span></td>
    	<td><label>Stop Trading Date: </label></td><td><span id="stopTradingDate" class="date"></span></td>
    </tr>
</table>
<hr class="data" style="display:none"/>
<small>WS status: <span id="result" style="font-weight:bold"></span></small><br/>
<small>Session ID: <%=session.getId()%></small><br/>

<%
    
	/*
	
	byte[] keyBytes = new byte[20];
	sr.nextBytes(keyBytes);
	SecretKey key = new SecretKeySpec(keyBytes, "HmacMD5");
	Mac m = Mac.getInstance("HmacMD5");
	m.init(key);
	m.update(new String("Hello").getBytes("UTF-8"));
	byte[] mac = m.doFinal();*/
	
	

	    
	

%>

		
</body>
</html>