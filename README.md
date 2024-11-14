# Website-to-APK
Wrap your website inside an android mobile app.

Built with Kotlin

## Getting started
- Open up the `MainActivity.kt`

```
app/src/main/java/com/acelords/web/MainActivity.kt
```

- Update the following to point to your website 
```kt

class MainActivity : AppCompatActivity() {
    private var showOrHideWebViewInitialUse = "show"
    private var myUrl: String = "https://acelords.com" // Change this  to your website hostname

```

- Update the app namespace from `com.acelords.web` to your own e.g. `com.mydomain.web`
- Compile your app

## Credits
- [SyntaxLexx](https://github.com/syntaxlexx)
- [AceLords](https://acelords.com)

## Security
For any security concerns, contact the following:
- [SyntaxLexx](mailto:syntaxlexx@gmail.com)
- [AceLords](mailto:support@acelords.com)


