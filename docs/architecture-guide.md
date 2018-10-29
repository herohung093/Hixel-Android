## Architecture

We use MVVM as outlined in [Google Architecture Guidelines](https://developer.android.com/jetpack/docs/guide), with a few of our own tweaks.

### Views

These consist almost solely of activities, this was an initial design decision to help combat the complexities of the Android lifecycle. However, since the introduction of Android Lifecycle components this has become a non-issue and we have begun to move towards the integration of fragments into our architecture. 

A long-term goal for our views is to have a master actvity with dynamic fragments.

### ViewModel

These control the logic to display particular aspects of the views and work to expose the data required by the view. 

Each ViewModel utilises LiveData to monitor the database for changes and seemlessly update our UI.

### Repository

__User repository__ handles interactions pertaining to adding and removing data for a particular user.

__Company repository__ stores any data for commonly searched companies, reducing the number of calls needed to be made to the server.

Both of these repositories act as an intemediary layer for handling more sophisticated interactions with the data. We implemented a resource delegation algorithm that allows us to pull data from the local db when required or retreive it from the server when the data has become stale or new data is required.



#### 