var id=1;
window.onload=function(){
	sendGetRequest();
}
function showTodoList()
{
	document.getElementById("user-content").style.display="none";
	document.getElementById("contact-content").style.display="none";
	document.getElementById("about-content").style.display="none";
	document.getElementById("todo-content").style.display="block";
}

function showUser() {
	document.getElementById("todo-content").style.display="none";
	document.getElementById("contact-content").style.display="none";
	document.getElementById("about-content").style.display="none";
	document.getElementById("user-content").style.display="block";
}
function showContact() {
	document.getElementById("user-content").style.display="none";
	document.getElementById("todo-content").style.display="none";
	document.getElementById("about-content").style.display="none";
	document.getElementById("contact-content").style.display="block";
}
function showAbout() {
	document.getElementById("user-content").style.display="none";
	document.getElementById("contact-content").style.display="none";
	document.getElementById("todo-content").style.display="none";
	document.getElementById("about-content").style.display="block";
}
function sendGetRequest()
  { 
     var request = new XMLHttpRequest();
     request.open('GET', 'http://localhost:2002/todos', true);
     request.onload = function () { display(this.responseText);}
     request.send();
  }
  function sendPostRequest(item)
  {  id++;
     var request = new XMLHttpRequest();
     request.open('POST', 'http://localhost:2002/todos', true);
     request.onload = function () { display(this.responseText)};
     request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
     var obj={"itemId":id,"itemName":item};
     request.send(JSON.stringify(obj));

     addToCurrentDisplay(obj);
 
 }
function sendDeleteRequest(rowId)
{
	 var request = new XMLHttpRequest();
     request.open('DELETE', 'http://localhost:2002/todos/'+rowId, true);
     request.send();
}
function sendPutRequest(itemId,item){
     var request = new XMLHttpRequest();
     request.open('PUT', 'http://localhost:2002/todos', true);
     request.onload = function () { display(this.responseText)};
     request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
     var obj={"itemId":itemId,"itemName":item};
     request.send(JSON.stringify(obj));	

}
function addToCurrentDisplay(obj){
		var table=document.getElementById("todo-table");
		var row=table.insertRow(-1);
		var cell0=row.insertCell(0);
		var cell1=row.insertCell(1);
		var cell2=row.insertCell(2);
		var pTag=document.createElement("p");
		pTag.innerHTML=obj.itemName;
		var pid=document.createElement("p");
		pid.innerHTML=obj.itemId;
		var inputBox=document.createElement("input");
		inputBox.setAttribute('type',"text");
		inputBox.style.display="none";

		cell0.appendChild(pid);
		cell1.appendChild(pTag);
		cell1.appendChild(inputBox);

		var edit=document.createElement("button");
		createEditButton(edit);

		var del=document.createElement("button");
		createDeleteButton(del);
		
		cell2.appendChild(edit);
		cell2.appendChild(del);

		id=obj.itemId;

		
		addToDataList(obj.itemName);
		document.getElementById("search-item").value="";
		showList();

}

function display(responseText)
{
	let jsObj=JSON.parse(responseText);

	var table=document.getElementById("todo-table");
	for (let i=0;i<jsObj.length;i++)
	{
		
		var row=table.insertRow(-1);
		var cell0=row.insertCell(0);
		var cell1=row.insertCell(1);
		var cell2=row.insertCell(2);
		var pTag=document.createElement("p");
		pTag.innerHTML=jsObj[i].itemName;
		var pid=document.createElement("p");
		pid.innerHTML=jsObj[i].itemId;
		var inputBox=document.createElement("input");
		inputBox.setAttribute('type',"text");
		inputBox.style.display="none";

		cell0.appendChild(pid);
		cell1.appendChild(pTag);
		cell1.appendChild(inputBox);

		var edit=document.createElement("button");
		createEditButton(edit);

		var del=document.createElement("button");
		createDeleteButton(del);
		
		cell2.appendChild(edit);
		cell2.appendChild(del);

		id=jsObj[i].itemId;
		addToDataList(jsObj[i].itemName);
	}
	showList();
}
function createEditButton(edit){
		edit.style.fontSize="24px";
		edit.style.marginRight = "4px";
		var editIcon=document.createElement("i");
		editIcon.setAttribute('class', "fa fa-edit");
		edit.setAttribute('onclick',"editData(this)")
		edit.appendChild(editIcon);
}
function createDeleteButton(del){
		del.style.fontSize="24px";
		var delIcon=document.createElement("i");
		delIcon.setAttribute('class', "fa fa-trash-o");
		del.setAttribute('onclick',"deleteRow(this)");
		del.appendChild(delIcon);

}
function isDuplicate(item){
	var rows=document.getElementById("todo-table").rows;

	for(let i = 1; i < rows.length; i++)
	{
		var td=rows[i].cells;
		var pTag=td[1].childNodes;
		var data=pTag[0].innerHTML;
		if(data==item)
		{
			return true;
		}	
	}
	return false;
}
function addToList(){
	var item=document.getElementById("search-item").value;
	if(item==null||item=='')
		return;

	if(isDuplicate(item)==true)
	{
		alert("Element already present");
		return;
	}

	sendPostRequest(item);

}
function showList(){
	var rows=document.getElementById("todo-table").rows;
	for(let i = 1; i < rows.length; i++)
	{
		rows[i].style.display="table-row";
	}
}

function addToDataList(item){
	var listItem=document.getElementById("listItems");
	var optionTag=document.createElement("option");
	optionTag.setAttribute('value',item);
	listItem.appendChild(optionTag);
}
function deleteRow(row) {
	var rowId=row.parentElement.parentElement.firstChild.textContent;
	sendDeleteRequest(rowId);
	console.log(rowId);
	row.parentElement.parentElement.remove();
}

function editData(element){

	var child=element.parentElement.previousElementSibling.childNodes;
	child[0].style.display="none";
	child[1].style.display="block";
	child[1].value = child[0].innerHTML;


	toggleToSave(element);

}
function toggleToSave(element)
{
	element.childNodes[0].setAttribute('class', "fa fa-save");
	element.setAttribute('onclick',"saveData(this)");

}

function toggleToEdit(element){
	element.childNodes[0].setAttribute('class', "fa fa-edit");
	element.setAttribute('onclick',"editData(this)");	
}

function saveData(element){
	var child=element.parentElement.previousElementSibling.childNodes;
	child[1].style.display="none";
	
	child[0].innerHTML=child[1].value;
	child[0].style.display="block";

	let itemId=element.parentElement.parentElement.firstChild.textContent;
	sendPutRequest(itemId,child[0].innerHTML);
	addToDataList(child[0].innerHTML);

	toggleToEdit(element);

}

function searchElement(){

	var searchItem=document.getElementById("search-item").value;
	
	if(searchItem==null||searchItem=='')
		return;
	
	var rows=document.getElementById("todo-table").rows;
	console.log(rows);
	for(let i = 1; i < rows.length; i++)
	{
		var td=rows[i].cells;
		var pTag=td[1].childNodes;
		var data=pTag[0].innerHTML;
		
		if(data!=searchItem)
			rows[i].style.display="none";
		else
			rows[i].style.display="table-row";
	}
}
