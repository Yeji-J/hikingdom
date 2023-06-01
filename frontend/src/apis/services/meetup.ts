import apiRequest from 'apis/AxiosInterceptor'
import { AxiosError, AxiosResponse } from 'axios'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import {
  MeetupMemberInfo,
  MeetupInfoDetail,
  MeetupReview,
} from 'types/meetup.interface'
import { ClubMember } from 'types/club.interface'
import toast from 'components/common/Toast'
import { useNavigate } from 'react-router-dom'

// 일정 상세 조회
export function useMeetupDetailQuery(clubId: number, meetupId: number) {
  return useQuery<any, AxiosError, MeetupInfoDetail>(
    ['meetup', clubId, meetupId],
    () => apiRequest.get(`/clubs/${clubId}/meetups/${meetupId}/detail`),
    { select: (res) => res?.data.result }
  )
}

// 일정 멤버 조회
export function useMeetupMemberQuery(clubId: number, meetupId: number) {
  return useQuery<any, AxiosError, MeetupMemberInfo>(
    ['meetupMembers', clubId, meetupId],
    () => apiRequest.get(`/clubs/${clubId}/meetups/${meetupId}/members`),
    { select: (res) => res?.data.result, enabled: !!clubId }
  )
}

// 일정 멤버 상세 조회
export function useMembersDetailQuery(clubId: number, meetupId: number) {
  return useQuery<any, AxiosError, ClubMember[]>(
    ['meetupMembersDetail', clubId, meetupId],
    () => apiRequest.get(`/clubs/${clubId}/meetups/${meetupId}/members/detail`),
    { select: (res) => res?.data.result, enabled: !!clubId }
  )
}

// 일정 참여
export function useJoinMeetup(clubId: number, meetupId: number) {
  const queryClient = useQueryClient()
  return useMutation(
    () => apiRequest.post(`/clubs/${clubId}/meetups/${meetupId}/join`),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['meetupMembers', clubId, meetupId])
        queryClient.invalidateQueries(['meetup'])
        toast.addMessage('success', '일정에 참여했습니다')
      },
      onError: (err: AxiosResponse) => {
        toast.addMessage('error', err.data.message)
      },
    }
  )
}

// 일정 참여 취소
export function useUnJoinMeetup(clubId: number, meetupId: number) {
  const queryClient = useQueryClient()
  return useMutation(
    () => apiRequest.delete(`/clubs/${clubId}/meetups/${meetupId}/join`),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['meetupMembers', clubId, meetupId])
        queryClient.invalidateQueries(['meetup'])
        toast.addMessage('success', '일정 참여를 취소했습니다')
      },
      onError: (err: AxiosResponse) => {
        toast.addMessage('error', err.data.message)
      },
    }
  )
}

// 일정 사진 조회 : Todo
export function getMeetupAlbum(
  clubId: number,
  meetupId: number,
  photoId: number | null = null,
  size: number | null = null
) {
  return apiRequest
    .get(`/clubs/${clubId}/meetups/${meetupId}/photos`, {
      params: {
        photoId,
        size,
      },
    })
    .then((res) => res.data.result)
}

// 일정 사진 등록
export function usePostMeetupPhoto(clubId: number, meetupId: number) {
  const queryClient = useQueryClient()
  return useMutation(
    (formData: FormData) =>
      apiRequest.post(`/clubs/${clubId}/meetups/${meetupId}/photos`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      }),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['meetupPhotos', clubId, meetupId])
        toast.addMessage('success', '사진이 추가되었습니다')
      },
    }
  )
}

// 일정 후기 조회
export function useMeetupReviewsQuery(clubId: number, meetupId: number) {
  return useQuery<any, AxiosError, MeetupReview[]>(
    ['meetupReviews', clubId, meetupId],
    () => apiRequest.get(`/clubs/${clubId}/meetups/${meetupId}/reviews`),
    { select: (res) => res?.data.result, enabled: !!clubId }
  )
}

// 일정 후기 등록
export function usePostReview(
  clubId: number,
  meetupId: number,
  content: string
) {
  const queryClient = useQueryClient()
  return useMutation(
    () =>
      apiRequest.post(`/clubs/${clubId}/meetups/${meetupId}/reviews`, {
        content,
      }),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['meetupReviews', clubId, meetupId])
        toast.addMessage('success', '후기가 등록되었습니다')
      },
    }
  )
}

// 일정 후기 삭제
export function useDeleteReview(
  clubId: number,
  meetupId: number,
  reviewId: number
) {
  const queryClient = useQueryClient()
  return useMutation(
    () =>
      apiRequest.delete(
        `/clubs/${clubId}/meetups/${meetupId}/reviews/${reviewId}`
      ),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['meetupReviews', clubId, meetupId])
        toast.addMessage('success', '후기가 삭제되었습니다')
      },
    }
  )
}

// 일정 삭제
export function useDeleteMeetup(clubId: number, meetupId: number) {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  return useMutation(
    () => apiRequest.delete(`/clubs/${clubId}/meetups/${meetupId}`),
    {
      onSuccess: () => {
        // queryClient.invalidateQueries([]) Todo: 쿼리키 적용
        toast.addMessage('success', '일정이 삭제되었습니다')
        navigate(`/club/main`)
      },
      onError: (err: any) => {
        if (err.status === 400) {
          toast.addMessage('error', '완료된 일정은 삭제할 수 없습니다')
        }
      },
    }
  )
}
