# App Heatmap


La app de heatmap es una aplicación motivada por la actual situación de la pandemia del covid, en la cual se pretende no crear grandes agrupaciones de personas para evitar los contagios. Esta pretende dar al usuario la posibilidad de conocer en cierta medida la ocupación de un lugar.

Su funcionamiento sería el siguiente, dada una ubicación introducida por el usuario, le proporcionará información acerca de este establecimiento, esta información será el nivel de personas que se encuentran es este lugar de una forma aproximada y en caso de no encontrar información para este lugar la posibilidad de poder obtener un estimación con los lugares circundantes a este.


Esta aplicación hace uso del patron de diseño model-view-presenter. Para su realización hace uso de la librería: [populartimes](https://github.com/m-wrzr/populartimes). La cual haced de interfaz con los servicios de googel para obtener los datos de las distintas ocupaciones de los locales. librería que esta en python y fue consumida a modo de consulta como un servicio rest, para lo cual usamos retrofit2 como librería de apoyo para las conexiones con este servidor.

[Presentación](https://github.com/lugman/Heatmap/blob/master/Presentacion.pdf)
Demo:
[![Ver](https://github.com/lugman/heatmap/blob/master/demo-img-video.png)](https://www.youtube.com/watch?v=eS5DTPN3TqA)
