Para ejecutar correctamente el archivo, debido a la dependencia de librerías de FiltroPrecio y que trabajamos en una versión superior a Java 11, se debe hacer lo siguiente:

Run -> Run Configurations -> Arguments : Donde pone VM Arguments pondremos el siguiente comando:
--add-opens=java.desktop/com.sun.java.swing.plaf.windows=ALL-UNNAMED
Y le damos a Apply and Close

Para el desarrollo de la aplicación haremos eso, una vez terminado el proyecto habrá que crear un script que lanze la aplicación con esa flag.

(Resumen de la conclusión sacada a partir de lo hablado con ChatGPT)