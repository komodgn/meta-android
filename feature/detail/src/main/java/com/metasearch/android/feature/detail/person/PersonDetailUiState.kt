package com.metasearch.android.feature.detail.person

import android.net.Uri
import com.metasearch.android.core.model.Person
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PersonDetailUiState(
    val isLoading: Boolean = false,
    val person: Person? = null,
    val photoUris: ImmutableList<Uri> = persistentListOf(),
    val showEditDialog: Boolean = false,
    val showMergeConfirmDialog: Boolean = false,
    val editName: String = "",
    val editPhone: String = "",
    val editIsHomeDisplay: Boolean = false,
    val editRepresentativeFaceId: Long? = null,
    val showPhotoSelectDialog: Boolean = false,
    val eventSink: (PersonDetailUiEvent) -> Unit,
) : CircuitUiState {
    companion object
}

sealed interface PersonDetailUiEvent : CircuitUiEvent {
    /**
     * 헤더의 뒤로 가기 버튼 클릭
     */
    data object OnHeaderBackClick : PersonDetailUiEvent

    /**
     * 헤더 메뉴 클릭 이벤트
     */
    data object OnMenuClick : PersonDetailUiEvent

    /**
     * 인물 정보 업데이트 요청 이벤트
     */
    data object OnEditSaveClick : PersonDetailUiEvent

    data class OnEditNameChange(
        val name: String,
    ) : PersonDetailUiEvent

    data class OnEditPhoneChange(
        val phone: String,
    ) : PersonDetailUiEvent

    data class OnEditHomeDisplayChange(
        val isHomeDisplay: Boolean,
    ) : PersonDetailUiEvent

    /**
     * 인물 이름 중복 저장 확인 다이얼로그 여는 이벤트
     */
    data object OnConfirmMergeSave : PersonDetailUiEvent

    /**
     * 인물 이름 중복 저장 확인 다이얼로그 닫는 이벤트
     */
    data object OnDismissMergeDialog : PersonDetailUiEvent

    /**
     * 클릭한 사진으로 프로필 이미지 변경
     */
    data class OnEditThumbnailClick(
        val faceId: Long,
    ) : PersonDetailUiEvent

    /**
     * 인물 정보 수정 다이얼로그 닫는 이벤트
     */
    data object OnEditCancel : PersonDetailUiEvent

    /**
     * 프로필 사진 선택 다이얼로그 여는 이벤트
     */
    data object OnThumbnailClick : PersonDetailUiEvent

    /**
     * 프로필 사진 선택 다이얼로그 닫는 이벤트
     */
    data object OnPhotoSelectCancel : PersonDetailUiEvent

    /**
     * 그리드 영역 이미지 클릭 시, 사진 상세 화면으로 이동
     */
    data class OnGridImageClick(
        val imageUri: Uri,
    ) : PersonDetailUiEvent
}
