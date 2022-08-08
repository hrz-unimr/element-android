/*
 * Copyright (c) 2021 New Vector Ltd
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

package im.vector.umr.core.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import im.vector.umr.core.dialogs.UnrecognizedCertificateDialog
import im.vector.umr.core.error.ErrorFormatter
import im.vector.umr.core.time.Clock
import im.vector.umr.features.analytics.AnalyticsTracker
import im.vector.umr.features.call.webrtc.WebRtcCallManager
import im.vector.umr.features.home.AvatarRenderer
import im.vector.umr.features.navigation.Navigator
import im.vector.umr.features.pin.PinLocker
import im.vector.umr.features.rageshake.BugReporter
import im.vector.umr.features.session.SessionListener
import im.vector.umr.features.settings.VectorPreferences
import im.vector.umr.features.ui.UiStateRepository
import kotlinx.coroutines.CoroutineScope

@InstallIn(SingletonComponent::class)
@EntryPoint
interface SingletonEntryPoint {

    fun sessionListener(): SessionListener

    fun avatarRenderer(): AvatarRenderer

    fun activeSessionHolder(): ActiveSessionHolder

    fun unrecognizedCertificateDialog(): UnrecognizedCertificateDialog

    fun navigator(): Navigator

    fun clock(): Clock

    fun errorFormatter(): ErrorFormatter

    fun bugReporter(): BugReporter

    fun vectorPreferences(): VectorPreferences

    fun uiStateRepository(): UiStateRepository

    fun pinLocker(): PinLocker

    fun analyticsTracker(): AnalyticsTracker

    fun webRtcCallManager(): WebRtcCallManager

    fun appCoroutineScope(): CoroutineScope
}
