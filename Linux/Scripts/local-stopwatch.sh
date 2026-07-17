#!/usr/bin/zsh
systemd-inhibit --what=idle:sleep --why="Stop watch Running" zsh -fc '
  set -eE
  local sleep
  local time_min=(0)

  zparseopts -D -F -K -- \
      {s,-sleep}=sleep \
      {t,-time-min}:=time_min || return
        #${var[-1]} gets variable 
  if [[ "${time_min[-1]}" -le 0 ]]; then
    print -P "Please insert time"
    return 1
  fi 
  kitten @ detach-tab
  clear && printf "\e[3J"
  tput civis
  #not the best fix, but fixes infinite loop -- 01-07-2026
  if (($#sleep)); then 
    print -P "Sleep mode!"
  else;
    print -P "Timer mode"
  fi 
  for ((i="${time_min[-1]}"; i>0; i--))
  do
    echo -ne "  $i minutes remaining \033[0K\r"
    sleep 60
  done
  
  echo -ne "TIMES UP \033[0K\r"
  if (($#sleep)); then 
   systemctl suspend -i
    return 0
  fi
  while; do
  pw-play /usr/share/sounds/ocean/stereo/alarm-clock-elapsed.oga
  done
  ' local-stopwatch-inner "$@"
