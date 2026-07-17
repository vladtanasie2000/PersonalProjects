# Desc 
Small linux script designed to work as a stopwatch, with input being given using parameters
The inputs are : 
  * -t : time in minutes 
  * -s : sleep mode

It will keep the machine active during the time of the stopwatch by issuing `systemd-inhibit`
To be able to use `systemd-inhibit` I needed to wrap my script and run it using `zsh -fc`

It can operate in two modes 
  * alarm (default) : after it reaches the endtime it will start an alarm
  * * *NOTE* the alarm will keep triggering until Ctrl-C interupt has been sent
  * sleep : it will automatically turn the computer to sleep 
  * * *NOTE* we need to add `systemctl-suspend` to sudoers otherwise it will request a password

This script was made because I needed to keep track of time, and as such I have used some functionalities
(such as kitten detach to detach my tab into another window) that aren't standard

