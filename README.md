# Battlenet OAuth2
Library to log in Battlenet (Blizzard) with OAuth2 system.

# Version

0.0.2

# Installation

To use this library in your android project, just simply add the following dependency into your build.gradle

```sh
dependencies {
   compile 'com.github.dementhius:battlenet-oauth2:0.0.2'
}
```

Or if you prefer using Maven, you just only need to add the following dependency in your pom.xml

```sh
<dependency>
  <groupId>com.github.dementhius</groupId>
  <artifactId>battlenet-oauth2</artifactId>
  <version>0.0.2</version>
  <type>pom</type>
</dependency>
```

# Usage
### Not in APP
First of all, you have to create a developer account in Battle.net, you can sign up in [this link](https://dev.battle.net/member/register).

Once you´re registered, you have to add a new application in Battle.net Developer´s Portal. You must add a callback URL (by default in library we will use https://localhost).

Now, we have all we need, we just need to save the following data to use in the APP:

* Client Id
* Client Secret
* Callback URL

### APP

First of all, we need to grant internet permission in manifest.xml

```
 <uses-permission android:name="android.permission.INTERNET" />
```

Now we have to create a new BnOAuth2Params with your Battle.net Developer´s account parameters and selecting the Scope and Zone you want.
```
bnOAuth2Params = new BnOAuth2Params("Your client Id", "Your client secret", "zone", "Your callback URL", "App Name", Scopes...);
```

On the one hand, to select the zone, you just only need to select one of them from BnConstants
```
BnConstants.ZONE_EUROPE / BnConstants.ZONE_UNITED_STATES / BnConstants.ZONE_KOREA / BnConstants.ZONE_TAIWAN / BnConstants.ZONE_CHINA
```

On the other hand, you can add all the scopes you want, separated by commas (Nowadays, Blizzard only provides Starcraft 2 and World of Warcraft scopes).
```
BnConstants.SCOPE_WOW / BnConstants.SCOPE_WOW
```

Finally, we only need to start BnOAuthAccessTokenActivity sending the BnOAuth2Params we´ve just created and the activity where we want to be redirected at the end.
```
final Intent intent = new Intent(this, BnOAuthAccessTokenActivity.class);
// Send BnOAuth2Params
intent.putExtra(BnConstants.BUNDLE_BNPARAMS, bnOAuth2Params);
// Send redirect Activity
intent.putExtra(BnConstants.BUNDLE_REDIRECT_ACTIVITY, DestinyActivity.class);
startActivity(intent);
```

_Complete example [here](https://github.com/dementhius/battlenet-oauth2/tree/master/app)_

# Change Logs

### v-0.0.2

Added more zones (Korea, Taiwan and China)

### v-0.0.1

Initial version

# License

Apache 2.0