import React from 'react'
import AlarmItem from 'components/user/AlarmItem'
import styles from './AlarmList.module.scss'
import { Alarm } from 'types/user.interface'

function AlarmList({ alarmList }: { alarmList: Alarm[] }) {
  return (
    <div className={`${styles.alarms}`}>
      {alarmList.map((alarm) => (
        <AlarmItem key={alarm.notificationId} alarm={alarm} />
      ))}
    </div>
  )
}

export default AlarmList
