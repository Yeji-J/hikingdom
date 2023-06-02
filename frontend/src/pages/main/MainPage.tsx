import React from 'react'
import styles from './MainPage.module.scss'
import { useNavigate } from 'react-router-dom'
import { useTodayMountainsQuery } from 'apis/services/mountains'
import { useTodayClubMtQuery, useclubRankTop3Query } from 'apis/services/clubs'
import cloud from 'assets/images/cloud.png'
import trophy from 'assets/images/trophy.png'
import mountain from 'assets/images/mountain.png'
import MtList from 'components/common/MtList'
import IconText from 'components/common/IconText'
import RankList from 'components/common/RankList'
import Loading from 'components/common/Loading'
import ClubMountain from 'components/club/ClubMountain'
import ErrorMessage from 'components/common/ErrorMessage'

function MainPage() {
  const navigate = useNavigate()

  const {
    isLoading: isTodayMtLoading,
    isError: isTodayMtError,
    data: mtInfoArray,
  } = useTodayMountainsQuery()

  const {
    isLoading: isClubRankTop3Loading,
    isError: isClubRankTop3Error,
    data: clubRankTop3,
    isSuccess: isClubRankTop3Success,
  } = useclubRankTop3Query()

  const {
    isLoading: isTodayClubMtLoading,
    isError: isTodayClubMtError,
    data: todayClubMt,
    isSuccess: isTodayClubMtSuccess,
  } = useTodayClubMtQuery()

  if (isTodayMtLoading || isClubRankTop3Loading || isTodayClubMtLoading) {
    return <Loading />
  }

  if (isTodayMtError || isClubRankTop3Error || isTodayClubMtError) {
    return <ErrorMessage message="정보를 불러올 수 없습니다." />
  }

  return (
    <div className={styles.container}>
      <div className={styles.section}>
        <div className="p-md">
          <IconText
            imgSrc={cloud}
            text="여기 등산 어때요"
            size="md"
            isBold={true}
          />
        </div>
        <div className={styles.scroll}>
          <MtList mtInfoArray={mtInfoArray} size="sm" />
        </div>
      </div>
      <div className={styles.section}>
        <div className="p-md">
          <IconText imgSrc={trophy} text="TOP3" size="md" isBold={true} />
        </div>
        <div className={styles.scroll}>
          <RankList clubInfoArray={clubRankTop3.content} size="sm" />
        </div>
      </div>
      <div
        className={`${styles.section} ${styles.height}`}
        onClick={() => {
          navigate(`/club/${todayClubMt.clubId}/detail`)
        }}
      >
        <div className="p-md">
          <IconText
            imgSrc={mountain}
            text="오늘의 모임 산"
            size="md"
            isBold={true}
          />
        </div>
        <ClubMountain zoom={3.5} assetInfo={todayClubMt.assets} />
      </div>
    </div>
  )
}

export default MainPage
