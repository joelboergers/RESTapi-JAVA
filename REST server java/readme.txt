Globale Infos �ber den Server und die POSTMAN Methoden

Der Server wird �ber die Dialog.java Dateil gestartet.
Der Server w�rde mit POSTMAN getestet, die Methoden k�nnen geimportet werden(filename: "rest server.postman_collection.json").

Es gibt 5 Methoden:

localhost:4711

-> /get
	-> @return Gibt alle User wieder 
	-> @param Keine
	
-> /get/someone
	-> @return Gibt eine bestimten user wieder
	-> @param braucht eine parameter key:value  
		-> mit dem firstname : {"firstname":"value"} 
		-> mit dem lastname : {"lastname":"value"}
		
-> /add
	-> @return 
	-> @param Braucht einen einzigarten firstname 
		-> {"firstname":"value","lastname":"value"}
		
-> /edit
	-> @return
	-> @param Braucht eine key:value um den uUser zu finden, und eine key:value mit der neuen Value
		-> mit dem firstname : {"firstname":"Joel","newfirstname":"oui"}
		-> mit dem lastname : {"lastname":"Boergers","newlastname":"oui"}
	
-> /delete
	-> @return 
	-> @param Braucht eine key:value um den User zu finden und ihn zu l�schen 
		-> mit dem firstname : {"firstname":"value"}
		-> mit dem lastname : {"lastname":"value"}
	
	