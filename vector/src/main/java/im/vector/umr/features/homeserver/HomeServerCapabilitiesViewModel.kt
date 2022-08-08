/*
 * Copyright (c) 2020 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.umr.features.homeserver

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoints
import im.vector.umr.core.di.MavericksAssistedViewModelFactory
import im.vector.umr.core.di.SingletonEntryPoint
import im.vector.umr.core.di.hiltMavericksViewModelFactory
import im.vector.umr.core.platform.EmptyAction
import im.vector.umr.core.platform.EmptyViewEvents
import im.vector.umr.core.platform.VectorViewModel
import im.vector.umr.features.raw.wellknown.getElementWellknown
import im.vector.umr.features.raw.wellknown.isE2EByDefault
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.extensions.tryOrNull
import org.matrix.android.sdk.api.raw.RawService
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.homeserver.HomeServerCapabilities

class HomeServerCapabilitiesViewModel @AssistedInject constructor(
        @Assisted initialState: HomeServerCapabilitiesViewState,
        private val session: Session,
        private val rawService: RawService
) : VectorViewModel<HomeServerCapabilitiesViewState, EmptyAction, EmptyViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<HomeServerCapabilitiesViewModel, HomeServerCapabilitiesViewState> {
        override fun create(initialState: HomeServerCapabilitiesViewState): HomeServerCapabilitiesViewModel
    }

    companion object : MavericksViewModelFactory<HomeServerCapabilitiesViewModel, HomeServerCapabilitiesViewState> by hiltMavericksViewModelFactory() {

        override fun initialState(viewModelContext: ViewModelContext): HomeServerCapabilitiesViewState {
            val session = EntryPoints.get(viewModelContext.app(), SingletonEntryPoint::class.java).activeSessionHolder().getSafeActiveSession()
            return HomeServerCapabilitiesViewState(
                    capabilities = session?.homeServerCapabilitiesService()?.getHomeServerCapabilities() ?: HomeServerCapabilities()
            )
        }
    }

    init {

        initAdminE2eByDefault()
    }

    private fun initAdminE2eByDefault() {
        viewModelScope.launch(Dispatchers.IO) {
            val adminE2EByDefault = tryOrNull {
                rawService.getElementWellknown(session.sessionParams)
                        ?.isE2EByDefault()
                        ?: true
            } ?: true

            setState {
                copy(
                        isE2EByDefault = adminE2EByDefault
                )
            }
        }
    }

    override fun handle(action: EmptyAction) {}
}
