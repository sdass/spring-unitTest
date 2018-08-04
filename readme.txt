https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html

with standalone controller until test with mockito, junit4 and SprintRunner class
from controller upto service layer successful which mocking service.
Could not do repository to mock. revisit.

POST example
sdass@nybd-sdass MINGW64 /c/Users/sdass/Desktop
$ curl -X POST -H "Content-Type: application/json" -d '{"name": "someone", "value": "56778"}' "http://localhost:8086/spring-unitTest/postjson"
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100    69    0    32  100    37    256    296 --:--:-- --:--:-- --:--:--   296
{"name":"someone","value":"two"}
