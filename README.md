# Binger-app

Here is the project default project structure that i work with

Activities

Main Activity - This is the entry point of the application, you can navigate to popular tv shows s & tv shows airing today by using the menu (on options item selecrted)
Details Activity - This activity is responsible for showing the details of the a particular tv show when you click on it
Favorite Activity - This activity is responsible for showing the users favorite tv show, that is stored on the device
Adapters


Synchronize Service - This is an Intent Service responsible for performing favorite shows synchronization with TMDB
Data

AppDatabase - This is a RoomDatabase responsible for local storage of favorite tv shows.
ResponseModels - Data model objects, that model json responses from TMDB server
FavoriteShow - Data model object for local storage of favorite tv shows

