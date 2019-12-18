# vacancies_checker for Android

### What's this?
Basically the project is an implementation of [vacancy checker](https://github.com/pavel-nikiforov/vacancies_checker)
script as an Android application.

### How to use the application?
- (optional) Check out application settings
  Settings are available from main menu or action menu on start page
- Push **CHECK FOR UPDATES** button
- Wait untill update finishes, then press **VIEW RESULTS**
- On **Statistics** page select a category to view list of corresponding vacancies
- Select a vacancy to proceed to **Vacancy Details** page
  There is some useful information about the vacancy:
  - **Added:** the date when the vacancy was added to database in the first place
  - **Updated:** the last date when the vacancy reappeared in feed
  - **Updates count** shows how many times the vacancy reappeared
  
### Settings
##### Start URL
You can set your own start URL, it must point to a HH search result feed in order to work.

How to set up your own:
- open a _desktop_ browser (it's important)
- proceed to [HH](https://hh.ru/locale?language=EN)
- select your location
- proceed to [Advaced Search page](https://hh.ru/search/vacancy/advanced)
- select your industry (and perhaps other search criteria, but industry is crucial)
- press **Find**
- you are on search result page, **_save this URL_**
- open the app and paste this URL into settings
- test the URL by pressing **TEST IN BROWSER** button - if it's showing a list of vacancies then it's good to go

##### Keywords
When the app encounters a vacancy it puts the vacancy in either Accepted or Rejected lists. If the vacancy name contains one of keywords - we accept it.
You can delete existing keywords or add your own - those controls are obvious
> Please note that keywords are case sensitive

##### Don't forget to save your settings
There is no autosave

And one more thing, there is **Reset Settings** option in action menu

