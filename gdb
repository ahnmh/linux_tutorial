gdb


r(run) : 프로그램 실행. 이미 실행중일 때는 재실행.
r <argument>  : 실행할 때 파라미터 설정

l : 소스 10줄 보기
l 15, 30 : 소스 15줄부터 30줄까지 보기
l <function name> 지정한 함수의 소스를 출력함
	다른 파일인 경우,
	l <file.c>:<function name>
set listsize <n> 명령어를 통해 출력되는 행의 갯수를 조절

i(info) line : 현재 디버깅중인 라인
i args : 현재 함수로 넘어온 파라미터
i program : 현재 디버깅중인 프로세스 정보
i local : 현재 지역변수
i variables : 전역변수


b(breakpoint) <Line>
b <function name>
 : set break
	다른 파일인 경우, 파일 이름을 따옴표를 써서 감싼 후,
	b '<file.c>'::<function name>
b 226 : 소스 줄 번호로 설정(l <function name> 으로 줄 번호 확인한 다음 해당 줄번호로 설정하면 됨)
b 10 if var == 0  //var 변수의 값이 0일때 10번 행에 설정
info b : list of all break
d : delete all break
d n : delete break number n
disable b 1 3 : 1,3번 break 비활성화
enable b 1 3 : 1,3번 break 비활성화
cond(condition) 8 i == 456 : break 번호 8은 i = 456일때 hit되게 함.
clear: 모든 breakpoint를 삭제함.
clear <행 번호>: 소스 코드의 행 번호 기준으로 breakpoint를 삭제함.


k(kill) : 프로그램 종료

s(step) : step into
si : 어셈블리 단위로 step into
n(next) : step over
ni : 어셈블리 단위로 step over
s <n> : 입력한 숫자만큼 진행 step into
n <n> : 입력한 숫자만큼 진행 step over
finish : step out
u(until) : 현재 루프를 빠져나감
return <value> : 함수의 남은 부분을 수행하지 않고 나감. value가 있으면 해당 값을 리턴하고 나감.
c(continue) : run to the next break


p(print) <변수> : 변수 출력,
만일 변수가 포인터라면
p *value : value가 가르키는 값이 출력
당연히 더블포인터 p **value 도 된다.
p (char *)buf : 형변환도 가능하다.
p $<레지스터> : 레지스터 값 확인
p <변수> = <값> : p로 값을 설정할 수도 있다.

display : 스톱될때마다 전체 변수를 출력해줌!!!
display <변수> : 스톱될때마다 한번씩 변수값을 출력해줌!!!
 display bss[0]
undisplay <번호> : display 할 때마다 해당 변수를 대신하는 번호가 생기는데 이 번호를 지정해서 display를 끌 수 있다.
또는 d display <번호>
display 설정한 변수 보기
i display


watch <value> : value값이 써질 때 멈춘다.
watch (z > 28) : z가 28보다 커지면 멈춘다.
rwatch <value> : value값이 읽힐 때 멈춘다.
awatch <value> : value값이 읽히거나 써질때 멈춘다.

d n : delete watch number n. 
 * info b하면 break 뿐만 아니라 watch 까지 같이 출력됨. 이 번호를 가지고 break 지우는 것과 동일하게 watch도 지울 수 있다.
info watchpoints : watch를 걸어논 변수 리스트 출력


info r : 레지스터 확인

x/<범위><출력format><단위> : 메모리 영역 덤프
x/16bx 0xb6dfdd80 : 0xb6dfdd80위치에서 byte 단위 16진수로 16개 출력
x/64wx 0xb6dfdd80 : 0xb6dfdd80위치에서 dword 단위 16진수로 64개 출력
x/16bc 0xb6dfdd80 : 0xb6dfdd80위치에서 byte 단위 ASCII로 16개 출력
x/16bd 0xb6dfdd80 : 0xb6dfdd80위치에서 char 단위 10진수로 16개 출력

disas <function> : 함수를 디스어셈블해서 보여줌.


f(frame) : 현재 스택 위치
f <number> : bt의 출력 결과를 기준으로 스택 프레임을 이동함.
up : 하위 프레임(0 -> 1)으로 이동
down : 상위 프레임(1 -> 0)으로 이동

x 12i $pc : RIP가 현재 가르키는 위치부터 어셈블리어 12줄까지 보여줌

set disassembly-flavor intel : 어셈블리 코드 형식을 Intel 형식으로 바꿔서 보여줌.

pwd : 워킹 디렉토리 보기

directory
소스 코드 경로를 리셋한다. (`$cdir:$cwd')

set substitute-path
소스 코드 경로를 못찾으면 아래와 같이 나온다.
/media/ahnmh/76F2-12CB/api_wifi/src/wifi.c: No such file or directory
가끔 소스 코드 경로가 이상하게 설정되어 소스가 있는데 못찾는 경우가 발생함. 이때 사용.
set substitute-path /media/ahnmh/76F2-12CB/api_wifi/src /home/ahnmh/workspace/api_wifi/src


show directories
현재 소스 코드 경로 보기

call <함수(파라미터1, 파라미터2, ...)>
디버깅 중에 특정 함수를 호출해서 리턴 결과를 알 수 있다.
단, 해당 함수는 현재 수행중이지 않아야 한다.
예를 들어 함수 원형이 function(int a, int b)와 같다면,
call function(10, 20)과 같이 사용 가능함.


gdb <프로그램> -tui
프로그램을 tui 모드로 실행한다.
tui 없이 실행해도 Ctrl X + A로 tui 모드로 전환(토글 가능)할 수 있다.
 * welcome 스크린에서 가능하며 run하여 디버깅이 진행중일 때는 안됨.
 

디버깅 시점에 필요한 정보(break, watch, display 등) 자동으로 로드하기
 - 이전에 사용했던 디버깅 정보를 gdb를 새로 시작할 때 일일히 입력하면 매우 귀찮다.
 - 반복적으로 사용해야 하는 breakpoint등이 있다면,
 - .gdbinit 파일에 해당 명령을 입력한 후, test라는 프로세스를 디버깅할 때
 $ gdb -command=.gdbinit test 와 같이 실행한다.
 - .gdbinit 파일의 예
 ---------------------
 b myfunc
 display i
 
 
 
 
