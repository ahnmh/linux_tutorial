
리눅스의 부팅 단계

전원 ON -> 바이오스 단계 -> 부트 로더 단계 -> 커널 초기화 -> init 실행 -> 로그인 프롬프트 출력

바이오스 단계
 - 바이오스는 PC에 장착된 기본적인 하드웨어(키보드,디스크등)의 상태를 확인한 후 부팅 장치를 선택하여 부팅 디스크의 첫 섹터에서 512byte를 로딩한다.
 - 이 512byte를 마스터 부트 레코드(MBR)이라고 하며 디스크의 어느 파티션에 2차 부팅 프로그램(부트 로더)가 있는지에 대한 정보를 저장함.
 - 메모리에 로딩된 MBR은 부트 로더를 찾아 메모리에 로딩하는 작업까지 수행함.
 - 작업 순서
   하드웨어 검사 -> 부팅 장치 선택 -> MBR로드 -> 부트로더 로드 -> 부트 로더 단계

부트로더 단계
 - 부트로더는 일반적으로 여러 운영체제 중에서 부팅할 운영체제를 선택할 수 있는 메뉴를 제공함.
 - 우분투는 부트 로더로 GRUB를 사용함.
 - 멀티 부팅이 아닌 경우, GRUB는 메뉴를 출력하지 않고 바로 부팅 작업을 진행함.
 - 부트 로더는 리눅스 커널을 메모리에 로딩하는 역할을 수행한다. 리눅스 커널은 /boot 디렉토리 아래에 vmlinuz-버전명의 형태로 제공됨.

부팅할 때 GRUB를 출력하려면 아래 파일을 수정하면 된다.
sudo vi /etc/default/grub
...
GRUB_DEFAULT=0
#GRUB_HIDDEN_TIMEOUT=0 --> 이 값을 #로 주석처리하면 됨.
GRUB_HIDDEN_TIMEOUT_QUIET=true
GRUB_TIMEOUT=10
GRUB_DISTRIBUTOR=`lsb_release -i -s 2> /dev/null || echo Debian`
GRUB_CMDLINE_LINUX_DEFAULT="quiet splash"
GRUB_CMDLINE_LINUX=""
업데이트 이후에는 sudo update-grub을 수행해야 반영됨.

ahnmh@ahnmh-samsung-iot:~$ ls /boot/vm*
/boot/vmlinuz-3.19.0-25-generic  /boot/vmlinuz-3.19.0-51-generic
/boot/vmlinuz-3.19.0-49-generic  /boot/vmlinuz-3.19.0-56-generic

커널 초기화 단계
- 부트 로더에 의해 메모리에 로딩된 커널은 가장 먼저 시스템에 연결된 메모리, 디스크, 키보드, 마우스 등의 장치를 검사함.
: 리눅스를 처음 설치할 때 사용 가능한 하드웨어 정보를 미리 확인했다가 부팅할 때 이 장치들이 사용 가능한 상태를 유지되고 있는지 확인하는 것임.
- 이후 일반적으로 프로세스를 생성하는 방식인 fork를 사용하지 않고 생성되는 프로세스와 스레드를 생성함.(시스템 스레드??)
: 이 프로세스들은 대괄호로 표시되며 메모리 관리와 같은 커널의 동작을 수행하며 주로 낮은 번호의 PID를 배정받는다.
- 커널 프로세스가 생성되고 난후, init 실행 단계로 넘어감.
ahnmh@ahnmh-samsung-iot:~$ ps -ef|grep "\["
root         2     0  0 21:04 ?        00:00:00 [kthreadd]
root         3     2  0 21:04 ?        00:00:00 [ksoftirqd/0]
root         5     2  0 21:04 ?        00:00:00 [kworker/0:0H]
root         7     2  0 21:04 ?        00:00:04 [rcu_sched]
root         8     2  0 21:04 ?        00:00:00 [rcu_bh]
root         9     2  0 21:04 ?        00:00:01 [rcuos/0]
root        10     2  0 21:04 ?        00:00:00 [rcuob/0]
root        11     2  0 21:04 ?        00:00:00 [migration/0]
root        12     2  0 21:04 ?        00:00:00 [watchdog/0]
root        13     2  0 21:04 ?        00:00:00 [watchdog/1]
root        14     2  0 21:04 ?        00:00:00 [migration/1]
root        15     2  0 21:04 ?        00:00:00 [ksoftirqd/1]
root        17     2  0 21:04 ?        00:00:00 [kworker/1:0H]
root        18     2  0 21:04 ?        00:00:00 [rcuos/1]
root        19     2  0 21:04 ?        00:00:00 [rcuob/1]
root        20     2  0 21:04 ?        00:00:00 [watchdog/2]
...

init 실행 단계
- 다양한 서비스를 동작시킨다.
- 서비스가 시작되는 메시지를 볼려면 로그인 스플래시 화면에서 Ctrl + d를 누른다.
- 부팅할 때 메시지가 출력되게 하려면 sudo vi /etc/default/grub 에서 GRUB_CMDLINE_LINUX_DEFAULT="quiet splash"에서 quiet를 제거하면 됨.
- 부팅 메시지는 부팅 후에 dmesg|more 명령으로 확인할 수 있다.
 * 부팅 이후 장치 인식 정보등 유용한 정보가 많음.
ahnmh@ahnmh-samsung-iot:~$ dmesg|more
[    0.000000] Initializing cgroup subsys cpuset
[    0.000000] Initializing cgroup subsys cpu
[    0.000000] Initializing cgroup subsys cpuacct
[    0.000000] Linux version 3.19.0-56-generic (buildd@lgw01-10) (gcc version 4.8.2 (Ubuntu 4.8.2-19ubuntu1) ) #62~14.04
.1-Ubuntu SMP Fri Mar 11 11:03:15 UTC 2016 (Ubuntu 3.19.0-56.62~14.04.1-generic 3.19.8-ckt15)
[    0.000000] Command line: BOOT_IMAGE=/boot/vmlinuz-3.19.0-56-generic root=UUID=e6bca908-0fad-4901-9c9f-6a0a8594d89c r
o quiet splash vt.handoff=7
[    0.000000] KERNEL supported cpus:
[    0.000000]   Intel GenuineIntel
[    0.000000]   AMD AuthenticAMD
[    0.000000]   Centaur CentaurHauls
[    0.000000] e820: BIOS-provided physical RAM map:
[    0.000000] BIOS-e820: [mem 0x0000000000000000-0x000000000009cfff] usable
...
 - init프로세스는 처음 생성된 프로세스로 PID가 1번이다.



init 프로세스
- 서비스들를 실행하는 시스템 프로세스
- 유닉스에서 유래된 전통적인 init 프로세스는 스크립트를 순차적으로 실행하여 다른 프로세스들을 동작시켰다.
- 최근 우분투를 비롯한 리눅스 대부분이 이벤트 기반으로 동작하는 방식으로 init를 대체함.
- 우분투는 자체적으로 개발한 upstart를 init 대신 사용함.
- 전통적으로 init프로세스와 관련된 설정 파일이었던 /etc/inittab 파일은 사용하지 않음.
- 서비스를 실행하거나 종료하기 위해 스크립트 파일을 사용한다.
- 우분투는 upstart가 사용하는 스크립트와 함께 기존의 방식으로 동작하는 init 스크립트를 같이 사용한다.
- /etc/init 디렉토리와 /etc/init.d 디렉토리에 같은 서비스 파일이 있다면 /etc/init/ 디렉토리의 스크립트 파일이 우선 적용됨.

upstart 스크립트 제어
- upstart가 사용하는 스크립트 파일은 /etc/init 디렉토리에 작업명.conf 파일로 구성되어 있다.
ahnmh@ahnmh-samsung-iot:~$ ls /etc/init
acpid.conf                   failsafe-x.conf             mountnfs-bootclean.sh.conf        setvtrgb.conf
alsa-restore.conf            flush-early-job-log.conf    mountnfs.sh.conf                  shutdown.conf
alsa-state.conf              friendly-recovery.conf      mtab.sh.conf                      startpar-bridge.conf
- 전체 작업(서비스) 목록 출력
ahnmh@ahnmh-samsung-iot:~$ initctl list
gnome-keyring-gpg stop/waiting
indicator-application start/running, process 2145
unicast-local-avahi stop/waiting
update-notifier-crash stop/waiting
update-notifier-hp-firmware stop/waiting
xsession-init stop/waiting
dbus start/running, process 1909
...
특정 서비스 상태 보기
ahnmh@ahnmh-samsung-iot:~$ initctl status dbus 
dbus start/running, process 1909

작업 종료
ahnmh@ahnmh-samsung-iot:~$ initctl stop hud 
hud stop/waiting

작업 시
ahnmh@ahnmh-samsung-iot:~$ initctl start hud 
hud start/running, process 4713작



init 스크립트 제어
- 기존 init가 사용하는 스크립트 파일은 /etc/init.d 디렉토리에 존재한다.
- service 명령에서 start/stop등 서브 명령은 해당 스크립트에 지정된 값에 따라 적용된다(따라서 start/stop만 가능한 경우도 있음)
service 스크립트명 start/stop/restart/status
/etc/init.d/스크립트명 start/stop/restart/status
서비스 상태 보기
ahnmh@ahnmh-samsung-iot:~$ sudo service cups status
[sudo] password for ahnmh: 
cups start/running, process 2368
서비스 종료
ahnmh@ahnmh-samsung-iot:~$ sudo service cups stop
cups stop/waiting
서비스 시작
ahnmh@ahnmh-samsung-iot:~$ sudo service cups start
cups start/running, process 4869


init 프로세스의 런레벨
- init 프로세스는 시스템의 단계를 7개로 구분하여 정의하고 각 단계에 따라 서비스 셸 스크립트를 실행하는데, 이 단계를 런레벨이라고 함.
- 우분투(페도라는 다름)에서는 0, 1, 2, 6번 런레벨만 사용하며 기본 런레벨 = 2.
- 런레벨
 0 시스템 종료
 1 단일 사용자 모드
 2 그래피컬 다중 사용자 모드 + 네트워킹(기본값)
 3, 4, 5 = 런레벨 2와 동일
 6 시스템 재시작

런레벨 별로 실행하는 스크립트는 아래와 같이 확인할 수 있다. rc2.d ~ rc5.d의 내용이 동일하다.
ahnmh@ahnmh-samsung-iot:/etc$ ls rc*.d
rc0.d:
K10unattended-upgrades  K20postfix  K20speech-dispatcher  S20sendsigs  S31umountnfs.sh  S60umountroot
K20kerneloops           K20rsync    README                S30urandom   S40umountfs      S90halt

rc1.d:
K20kerneloops  K20rsync  K20speech-dispatcher  S30killprocs  S70pppd-dns
K20postfix     K20saned  README                S70dns-clean  S90single

rc2.d:
README         S20postfix  S20speech-dispatcher  S70dns-clean  S99grub-common  S99rc.local
S20kerneloops  S20rsync    S50saned              S70pppd-dns   S99ondemand

rc3.d:
README         S20postfix  S20speech-dispatcher  S70dns-clean  S99grub-common  S99rc.local
S20kerneloops  S20rsync    S50saned              S70pppd-dns   S99ondemand

rc4.d:
README         S20postfix  S20speech-dispatcher  S70dns-clean  S99grub-common  S99rc.local
S20kerneloops  S20rsync    S50saned              S70pppd-dns   S99ondemand

rc5.d:
README         S20postfix  S20speech-dispatcher  S70dns-clean  S99grub-common  S99rc.local
S20kerneloops  S20rsync    S50saned              S70pppd-dns   S99ondemand

rc6.d:
K10unattended-upgrades  K20postfix  K20speech-dispatcher  S20sendsigs  S31umountnfs.sh  S60umountroot
K20kerneloops           K20rsync    README                S30urandom   S40umountfs      S90reboot

rcS.d:
README  S25brltty  S37apparmor  S55urandom  S70x11-common

런레벨별로 실행하는 스크립트 파일은 /etc/init.d 디렉토리에 있는 파일에 대한 심볼릭 링크이다.

init 프로세스의 런레벨 변경하기
sudo init 1
- 우분투에서는 버그(System halt)가 있기 때문에 실행 불가함.
- 원래라면 그래픽 -> 콘솔로 터미널 환경이 바뀌면서 프롬프트가 출력되어야 함.

기본 런레벨 출력하기
cat init/rc-sysinit.conf 
...
env DEFAULT_RUNLEVEL=2
...

현재 런레벨 확인
ahnmh@ahnmh-samsung-iot:/etc$ runlevel 
N 2



시스템 종료
shutdown
sudo shutdown -h now : 시스템 즉시 종료
sudo shutdown -h +2 "System is going down in 2minutes." : 종료 메시지를 보여주고 2분 후에 종료함.
sudo shutdown -r +3 : 3분후에 재시작
sudo shutdown -c : 종료 명령 취소(다른 터미널에서 수행해야 함)
sudo shutdown -k 2 : 사용자들에게 종료 메시지만 보내고 실제로 종료하지는 않음

런레벨 변경하여 종료하기
sudo init 0 : 시스템 종료
sudo init 6 : 시스템 재시작

그밖에..
reboot = halt = poweroff
-f : 강제로 명령을 실행
-p : 시스템의 전원을 끔



데몬 프로세스
- 백그라운드에서 동작하면서 특정 서비스를 제공하는 프로세스
: 웹서버, 데이터베이스 서버, 원격 접속 서버 등.

데몬의 동작 방식
- standalone 데몬
: 항상 동작, 시스템의 자원을 낭비할 우려가 있음.
- 수퍼데몬에 의해 실행되는 데몬
: 평소에는 수퍼데몬만 동작하다가 서비스 요청이 오는 경우에만 실행됨. 독자형보다 서비스 응답 시간이 길수 있지만 자원을 효율적으로 사용할 수 있다. 

수퍼데몬
- xinetd
- 네트워크 서비스를 제공하는 데몬만 관리한다.
: 사용자가 네트워크 서비스를 요청하면 수퍼 데몬이 받아서 해당하는 서비스 데몬을 동작시킴.

데몬의 조상
- init와 커널 스레드 데몬
- init 데몬은 1번 프로세스이며 대부분의 프로세스의 조상이다.
ahnmh@ahnmh-samsung-iot:/etc$ pstree
init─┬─ModemManager───2*[{ModemManager}]
     ├─NetworkManager─┬─dhclient
     │                ├─dnsmasq
     │                └─3*[{NetworkManager}]
     ├─accounts-daemon───2*[{accounts-daemon}]
     ├─acpid
     ├─atd
     ├─bluetoothd
     ├─colord───2*[{colord}]
     ├─cron
     ├─cups-browsed
     ├─cupsd
     ├─dbus-daemon
     ├─6*[getty]
     ├─irqbalance
     ├─kerneloops
     ├─lightdm─┬─Xorg───{Xorg}
     │         ├─lightdm─┬─init─┬─at-spi-bus-laun─┬─dbus-daemon
     │         │         │      │                 └─3*[{at-spi-bus-laun}]
     │         │         │      ├─at-spi2-registr───{at-spi2-registr}
     │         │         │      ├─bamfdaemon───3*[{bamfdaemon}]
     │         │         │      ├─2*[dbus-daemon]
     │         │         │      ├─dconf-service───2*[{dconf-service}]
     │         │         │      ├─evolution-calen───4*[{evolution-calen}]
     │         │         │      ├─evolution-sourc───2*[{evolution-sourc}]
     │         │         │      ├─fcitx
     │         │         │      ├─fcitx-dbus-watc
     │         │         │      ├─gconfd-2
     │         │         │      ├─gedit───7*[{gedit}]
     │         │         │      ├─gnome-keyring-d───5*[{gnome-keyring-d}]
     │         │         │      ├─gnome-session─┬─compiz───3*[{compiz}]
     │         │         │      │               ├─deja-dup-monito───2*[{deja-dup-monito}]
     │         │         │      │               ├─nautilus───3*[{nautilus}]
     │         │         │      │               ├─nm-applet───2*[{nm-applet}]
     │         │         │      │               ├─polkit-gnome-au───2*[{polkit-gnome-au}]
     │         │         │      │               ├─telepathy-indic───2*[{telepathy-indic}]
     │         │         │      │               ├─unity-fallback-───2*[{unity-fallback-}]
     │         │         │      │               ├─update-notifier───3*[{update-notifier}]
     │         │         │      │               └─3*[{gnome-session}]
     │         │         │      ├─gnome-terminal─┬─bash───pstree
     │         │         │      │                ├─gnome-pty-helpe
     │         │         │      │                └─3*[{gnome-terminal}]
     │         │         │      ├─gvfs-afc-volume───2*[{gvfs-afc-volume}]
     │         │         │      ├─gvfs-gphoto2-vo───{gvfs-gphoto2-vo}
     │         │         │      ├─gvfs-mtp-volume───{gvfs-mtp-volume}
     │         │         │      ├─gvfs-udisks2-vo───2*[{gvfs-udisks2-vo}]
     │         │         │      ├─gvfsd───{gvfsd}
     │         │         │      ├─gvfsd-burn───{gvfsd-burn}
     │         │         │      ├─gvfsd-fuse───4*[{gvfsd-fuse}]
     │         │         │      ├─gvfsd-metadata───{gvfsd-metadata}
     │         │         │      ├─gvfsd-trash───2*[{gvfsd-trash}]
     │         │         │      ├─hud-service───3*[{hud-service}]
     │         │         │      ├─ibus-daemon─┬─ibus-dconf───3*[{ibus-dconf}]
     │         │         │      │             ├─ibus-engine-han───2*[{ibus-engine-han}]
     │         │         │      │             ├─ibus-engine-sim───2*[{ibus-engine-sim}]
     │         │         │      │             ├─ibus-ui-gtk3───3*[{ibus-ui-gtk3}]
     │         │         │      │             └─2*[{ibus-daemon}]
     │         │         │      ├─ibus-x11───3*[{ibus-x11}]
     │         │         │      ├─indicator-appli───{indicator-appli}
     │         │         │      ├─indicator-bluet───2*[{indicator-bluet}]
     │         │         │      ├─indicator-datet───5*[{indicator-datet}]
     │         │         │      ├─indicator-keybo───3*[{indicator-keybo}]
     │         │         │      ├─indicator-messa───3*[{indicator-messa}]
     │         │         │      ├─indicator-power───2*[{indicator-power}]
     │         │         │      ├─indicator-print───2*[{indicator-print}]
     │         │         │      ├─indicator-sessi───2*[{indicator-sessi}]
     │         │         │      ├─indicator-sound───3*[{indicator-sound}]
     │         │         │      ├─mission-control───2*[{mission-control}]
     │         │         │      ├─notify-osd───2*[{notify-osd}]
     │         │         │      ├─pulseaudio───3*[{pulseaudio}]
     │         │         │      ├─unity-panel-ser───2*[{unity-panel-ser}]
     │         │         │      ├─unity-settings-─┬─syndaemon
     │         │         │      │                 └─3*[{unity-settings-}]
     │         │         │      ├─2*[upstart-dbus-br]
     │         │         │      ├─upstart-event-b
     │         │         │      ├─upstart-file-br
     │         │         │      ├─window-stack-br
     │         │         │      ├─zeitgeist-daemo───{zeitgeist-daemo}
     │         │         │      ├─zeitgeist-datah───6*[{zeitgeist-datah}]
     │         │         │      └─zeitgeist-fts─┬─cat
     │         │         │                      └─2*[{zeitgeist-fts}]
     │         │         └─{lightdm}
     │         └─2*[{lightdm}]
     ├─master─┬─pickup
     │        └─qmgr
     ├─polkitd───2*[{polkitd}]
     ├─rsyslogd───3*[{rsyslogd}]
     ├─rtkit-daemon───2*[{rtkit-daemon}]
     ├─systemd-logind
     ├─systemd-udevd
     ├─udisksd───4*[{udisksd}]
     ├─upowerd───2*[{upowerd}]
     ├─upstart-file-br
     ├─upstart-socket-
     ├─upstart-udev-br
     ├─whoopsie───2*[{whoopsie}]
     └─wpa_supplicant


커널 스레드 데몬
- 커널의 일부분을 프로세스처럼 관리하는 데몬을 커널 데몬이라고 한다.
: []기호로 둘러싸여 있는 프로세스
: 대부분 입출력이나 메모리 관리, 디스크 동기화 등을 수행함.
: 대체로 낮은 PID
: 일반 프로세스의 조상 데몬이 init이라면 모든 커널 데몬의 PPID는 커널 스레드 데몬(kthread)임.

주요 데몬
- atd : 특정 시간에 실행하도록 예약한 명령을 실행함(at 명령으로 작업 등록)
- crond : 주기적으로 실행하도록 예약한 명령을 실행
- dhcpd : 동적으로 IP주소를 부여할 수 있도록 하는 서비스
- httpd : 웹서비스
- lpd : 프린터 서비스
- nfs : 네트워크 파일 서비스
- named : DNS 서비스
- sendmail : 이메일 서비스
- smtpd : 메일 전송 서비스
- popd : 기본 편지함 서비스
- routed : 자동 IP 라우터 테이블 서비스
- sshd : 원격 보안 접속 서비스
- ftpd : 파일 송수신 서비스
- ntpd : 시간 동기화 서비스
- syslogd : 로그 기록 서비스


서비스 등록

스크립트 예제
 802  vi hello

#!/bin/bash

PATH=/sbin:/bin:/usr/sbin:/usr/bin

start() {
    Hello
}

stop() {
    sudo killall -9 Hello
}

case $1 in
     start|stop) $1;;
     restart) stop; start;;
      *) echo "Run as $0 "; exit 1;;
esac

스크립트 파일을 /etc/init.d/에 복사
sudo cp hello /etc/init.d/
실행가능한 파일로 변경
sudo chmod 755 /etc/init.d/hello
서비스 등록
sudo update-rc.d  hello defaults
서비스 시작
service hello start
서비스 중지
service hello stop
서비스 삭제
sudo update-rc.d -f hello remove

* 참조
http://ezcocoa.com/?p=2376







