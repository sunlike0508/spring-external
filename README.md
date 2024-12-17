# 외부설정과 프로필1

## 외부설정이란

하나의 애플리케이션을 여러 다른 환경에서 사용해야 할 때가 있다. 

대표적으로 개발이 잘 진행되고 있는지 내부에서 확 인하는 용도의 개발 환경, 그리고 실제 고객에게 서비스하는 운영 환경이 있다.

* 개발 환경: 개발 서버, 개발 DB 사용 
* 운영 환경: 운영 서버, 운영 DB 사용
* 
문제는 각각의 환경에 따라서 서로 다른 설정값이 존재한다는 점이다. 

예를 들어서 애플리케이션이 개발DB에 접근하려 면 `dev.db.com` 이라는 url 정보가 필요한데, 운영DB에 접근하려면 `prod.db.com` 이라는 서로 다른 url을 사용해야 한다.

이 문제를 해결하는 가장 단순한 방법은 다음과 같이 각각의 환경에 맞게 애플리케이션을 빌드하는 것이다.

<img width="684" alt="Screenshot 2024-12-16 at 22 44 40" src="https://github.com/user-attachments/assets/34bf8441-29df-4abc-bf45-914fe3ff4963" />

* 개발 환경에는 `dev.db.com` 이 필요하므로 이 값을 애플리케이션 코드에 넣은 다음에 빌드해서 `개발 app.jar` 를 만든다.
* 운영 환경에는 `prod.db.com` 이 필요하므로 이 값을 애플리케이션 코드에 넣은 다음에 빌드해서 `운영 app.jar` 를 만든다.

이렇게 하면 각각의 환경에 맞는 `개발app.jar` , `운영app.jar` 가 만들어지므로 해당 파일들을 각 환경별로 배포하면 된다.

하지만 이것은 다음과 이유로 좋은 방법이 아니다. 
* 환경에 따라서 빌드를 여러번 해야 한다.
* 개발 버전과 운영 버전의 빌드 결과물이 다르다. 
  * 따라서 개발 환경에서 검증이 되더라도 운영 환경에서 다른 빌드 결과를 사용하기 때문에 예상치 못한 문제가 발생할 수 있다. 
  * 개발용 빌드가 끝나고 검증한 다음에 운영용 빌드를 해야 하는데 그 사이에 누군가 다른 코드를 변경할 수도 있다. 
  * 한마디로 진짜 같은 소스코드에서 나온 결과물 인지 검증하기가 어렵다.
* 각 환경에 맞추어 최종 빌드가 되어 나온 빌드 결과물은 다른 환경에서 사용할 수 없어서 유연성이 떨어진다. 
  * 향후 다른 환경이 필요하면 그곳에 맞도록 또 빌드를 해야 한다.

그래서 보통 다음과 같이 빌드는 한번만 하고 각 환경에 맞추어 **실행 시점에 외부 설정값을 주입**한다.

<img width="697" alt="Screenshot 2024-12-16 at 22 46 57" src="https://github.com/user-attachments/assets/3189477c-d577-4dc5-9a8d-0ebea2e0e59b" />

* 배포 환경과 무관하게 하나의 빌드 결과물을 만든다. 여기서는 `app.jar` 를 빌드한다. 이 안에는 설정값을 두지 않는다.
* 설정값은 실행 시점에 각 환경에 따라 외부에서 주입한다.
  * 개발 서버: `app.jar` 를 실행할 때 `dev.db.com` 값을 외부 설정으로 주입한다. 
  * 운영 서버: `app.jar` 를 실행할 때 `prod.db.com` 값을 외부 설정으로 주입한다.

이렇게 하면 빌드도 한번만 하면 되고, 개발 버전과 운영 버전의 빌드 결과물이 같기 때문에 개발환경에서 검증되면 운영 환경에서도 믿고 사용할 수 있다. 

그리고 이후에 새로운 환경이 추가되어도 별도의 빌드 과정 없이 기존 `app.jar` 를 사용해서 손쉽게 새로운 환경을 추가할 수 있다.

**유지보수하기 좋은 애플리케이션 개발의 가장 기본 원칙은 변하는 것과 변하지 않는 것을 분리하는 것이다.** 

유지보수하기 좋은 애플리케이션을 개발하는 단순하면서도 중요한 원칙은 **변하는 것과 변하지 않는 것을 분리**하는 것이다. 

각 환경에 따라 변하는 외부 설정값은 분리하고, 변하지 않는 코드와 빌드 결과물은 유지했다. 

덕분에 빌드 과정을 줄이고, 환경에 따른 유연성을 확보하게 되었다.

### 외부 설정

애플리케이션을 실행할 때 필요한 설정값을 외부에서 어떻게 불러와서 애플리케이션에 전달할 수 있을까?

<img width="699" alt="Screenshot 2024-12-16 at 22 49 58" src="https://github.com/user-attachments/assets/f556410f-71b3-4fea-9639-178204ba9317" />

외부 설정은 일반적으로 다음 4가지 방법이 있다.
* OS 환경 변수: OS에서 지원하는 외부 설정, 해당 OS를 사용하는 모든 프로세스에서 사용
* 자바 시스템 속성: 자바에서 지원하는 외부 설정, 해당 JVM안에서 사용
* 자바 커맨드 라인 인수: 커맨드 라인에서 전달하는 외부 설정, 실행시 `main(args)` 메서드에서 사용 
* 외부 파일(설정 데이터): 프로그램에서 외부 파일을 직접 읽어서 사용
  * 애플리케이션에서 특정 위치의 파일을 읽도록 해둔다. 예) `data/hello.txt` 
  * 그리고 각 서버마다 해당 파일안에 다른 설정 정보를 남겨둔다.
    * 개발 서버 `hello.txt` : `url=dev.db.com` 
    * 운영 서버 `hello.txt` : `url=prod.db.com`

먼저 앞의 3가지를 알아보자. 외부 파일(설정 데이터)은 뒤에서 설명한다.

## 외부 설정 - OS 환경 변수

OS 환경 변수(OS environment variables)는 해당 OS를 사용하는 모든 프로그램에서 읽을 수 있는 설정값이다. 

한마디로 다른 외부 설정과 비교해서 사용 범위가 가장 넓다.

**조회 방법**
* 윈도우 OS: `set`
* MAC, 리눅스 OS: `printenv`

**printenv** 실행 결과

```shell
➜  spring-external git:(main) printenv
TERM_SESSION_ID=w1t0p0:20974025-15BF-4C60-BA7F-73153B5A1AB3
SSH_AUTH_SOCK=/private/tmp/com.apple.launchd.9q4FLyPtzi/Listeners
LC_TERMINAL_VERSION=3.5.9
COLORFGBG=7;0
ITERM_PROFILE=Default
SQLITE_EXEMPT_PATH_FROM_VNODE_GUARDS=/Users/seonhoshin/Library/WebKit/Databases
XPC_FLAGS=0x0
LANG=ko_KR.UTF-8
PWD=/Users/seonhoshin/javacode/spring/springboot/spring-external
SHELL=/bin/zsh
__CFBundleIdentifier=com.googlecode.iterm2
SECURITYSESSIONID=186ab
TERM_FEATURES=T3LrMSc7UUw9Ts3BFGsSyHNoSxF
TERM_PROGRAM_VERSION=3.5.9
TERM_PROGRAM=iTerm.app
PATH=/usr/local/bin:/System/Cryptexes/App/usr/bin:/usr/bin:/bin:/usr/sbin:/sbin:/var/run/com.apple.security.cryptexd/codex.system/bootstrap/usr/local/bin:/var/run/com.apple.security.cryptexd/codex.system/bootstrap/usr/bin:/var/run/com.apple.security.cryptexd/codex.system/bootstrap/usr/appleinternal/bin:/Applications/iTerm.app/Contents/Resources/utilities
LC_TERMINAL=iTerm2
COLORTERM=truecolor
COMMAND_MODE=unix2003
TERM=xterm-256color
TERMINFO_DIRS=/Applications/iTerm.app/Contents/Resources/terminfo:/usr/share/terminfo
HOME=/Users/seonhoshin
TMPDIR=/var/folders/5q/y03lgw592353dvzjthlx785r0000gn/T/
USER=seonhoshin
XPC_SERVICE_NAME=0
LOGNAME=seonhoshin
LaunchInstanceID=47ED10C9-9DBD-476D-8027-248BA094A50C
__CF_USER_TEXT_ENCODING=0x0:3:51
ITERM_SESSION_ID=w1t0p0:20974025-15BF-4C60-BA7F-73153B5A1AB3
SHLVL=1
OLDPWD=/Users/seonhoshin/javacode/spring/springboot
ZSH=/Users/seonhoshin/.oh-my-zsh
PAGER=less
LESS=-R
LSCOLORS=Gxfxcxdxbxegedabagacad
LS_COLORS=di=1;36:ln=35:so=32:pi=33:ex=31:bd=34;46:cd=34;43:su=30;41:sg=30;46:tw=30;42:ow=30;43
_=/usr/bin/printenv
➜  spring-external git:(main) ✗
```

**설정 방법**

OS환경변수의 값을 설정하는 방법은 `윈도우 환경 변수` , `mac 환경 변수` 등으로 검색해보자.

수많은 예시를 확인할 수 있다.

애플리케이션에서 OS 환경 변수의 값을 읽어보자.

```java
@Slf4j
public class OsEnv {

    public static void main(String[] args) {
        Map<String, String> getenv = System.getenv();

        getenv.forEach((key, value) -> {
            log.info("key={} value={}", key, value);
        });
    }
}
```

```shell
23:00:38.719 [main] INFO hello.external.OsEnv - key=PATH value=/usr/local/bin:/System/Cryptexes/App/usr/bin:/usr/bin:/bin:/usr/sbin:/sbin:/var/run/com.apple.security.cryptexd/codex.system/bootstrap/usr/local/bin:/var/run/com.apple.security.cryptexd/codex.system/bootstrap/usr/bin:/var/run/com.apple.security.cryptexd/codex.system/bootstrap/usr/appleinternal/bin
23:00:38.721 [main] INFO hello.external.OsEnv - key=__CFBundleIdentifier value=com.jetbrains.intellij
23:00:38.721 [main] INFO hello.external.OsEnv - key=SHELL value=/bin/zsh
23:00:38.721 [main] INFO hello.external.OsEnv - key=PAGER value=less
23:00:38.721 [main] INFO hello.external.OsEnv - key=LSCOLORS value=Gxfxcxdxbxegedabagacad
23:00:38.721 [main] INFO hello.external.OsEnv - key=OLDPWD value=/

```

`System.getenv()` 를 사용하면 전체 OS 환경 변수를 `Map` 으로 조회할 수 있다. 

`System.getenv(key)` 를 사용하면 특정 OS 환경 변수의 값을 `String` 으로 조회할 수 있다.

OS 환경 변수를 설정하고, 필요한 곳에서 `System.getenv()` 를 사용하면 외부 설정을 사용할 수 있다.

이제 데이터베이스 접근 URL과 같은 정보를 OS 환경 변수에 설정해두고 읽어들이면 된다.

예를 들어서 개발 서버에서는 `DBURL=dev.db.com` 과 같이 설정하고, 운영 서버에서는 `DBURL=prod.db.com` 와 같이 설정하는 것이다.

이렇게 하면 `System.getenv("DBURL")` 을 조회할 때 각각 환경에 따라서 서로 다른 값을 읽게 된다.

하지만 OS 환경 변수는 이 프로그램 뿐만 아니라 다른 프로그램에서도 사용할 수 있다. 

쉽게 이야기해서 전역 변수 같은 효과가 있다. 

여러 프로그램에서 사용하는 것이 맞을 때도 있지만, 해당 애플리케이션을 사용하는 자바 프로그램 안에서만 사용되는 외부 설정값을 사용하고 싶을 때도 있다. 

다음에는 특정 자바 프로그램안에서 사용하는 외부 설정을 알 아보자.

## 외부 설정 - 자바 시스템 속성

자바 시스템 속성(Java System properties)은 실행한 JVM 안에서 접근 가능한 외부 설정이다. 

추가로 자바가 내부 에서 미리 설정해두고 사용하는 속성들도 있다.

자바 시스템 속성은 다음과 같이 자바 프로그램을 실행할 때 사용한다. 
* 예) `java -Durl=dev -jar app.jar`
* `-D` VM 옵션을 통해서 `key=value` 형식을 주면 된다. 이 예제는 `url=dev` 속성이 추가된다. 
* 순서에 주의해야 한다. `-D` 옵션이 - `jar` 보다 앞에 있다.

```java
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaSystemProperties {

    public static void main(String[] args) {
        Properties properties = System.getProperties();

        for(Object key : properties.keySet()) {
            log.info("prop {}={}", key, properties.get(key));
            log.info("prop {}={}", key, System.getProperty(String.valueOf(key)));
        }

        String url = System.getProperty("url");
        String username = System.getProperty("username");
        String password = System.getProperty("password");
  
        log.info("url={}", url);
        log.info("username={}", username);
        log.info("password={}", password);
    }
}
```

```text
22:24:21.618 [main] INFO hello.external.JavaSystemProperties - prop java.specification.version=17
22:24:21.619 [main] INFO hello.external.JavaSystemProperties - prop java.specification.version=17
22:24:21.619 [main] INFO hello.external.JavaSystemProperties - prop sun.jnu.encoding=UTF-8
22:24:21.619 [main] INFO hello.external.JavaSystemProperties - prop sun.jnu.encoding=UTF-8
...
22:28:00.889 [main] INFO hello.external.JavaSystemProperties - url=devdb
22:28:00.889 [main] INFO hello.external.JavaSystemProperties - username=dev_user
22:28:00.889 [main] INFO hello.external.JavaSystemProperties - password=dev_pw
```

**Jar 실행**

`jar` 로 빌드되어 있다면 실행시 다음과 같이 자바 시스템 속성을 추가할 수 있다.

`java -Durl=devdb -Dusername=dev_user -Dpassword=dev_pw -jar app.jar`

**자바 시스템 속성을 자바 코드로 설정하기**

자바시스템 속성은 앞서본 것처럼 `-D` 옵션을 통해 실행 시점에 전달하는 것도 가능하고, 다음과 같이 자바 코드 내부 에서 추가하는 것도 가능하다. 

코드에서 추가하면 이후에 조회시에 값을 조회할 수 있다.

* 설정: `System.setProperty(propertyName, "propertyValue")` 
* 조회: `System.getProperty(propertyName)`

* 참고로 이 방식은 코드 안에서 사용하는 것이기 때문에 외부로 설정을 분리하는 효과는 없다.














