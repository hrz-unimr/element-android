/*
 * Copyright 2018 New Vector Ltd
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

package im.vector.umr.features.onboarding.ftueauth.terms

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.args
import im.vector.umr.core.extensions.cleanup
import im.vector.umr.core.extensions.configureWith
import im.vector.umr.core.extensions.toReducedUrl
import im.vector.umr.core.utils.openUrlInChromeCustomTab
import im.vector.umr.databinding.FragmentLoginTermsBinding
import im.vector.umr.features.login.terms.LocalizedFlowDataLoginTermsChecked
import im.vector.umr.features.login.terms.LoginTermsViewState
import im.vector.umr.features.login.terms.PolicyController
import im.vector.umr.features.onboarding.OnboardingAction
import im.vector.umr.features.onboarding.OnboardingViewState
import im.vector.umr.features.onboarding.RegisterAction
import im.vector.umr.features.onboarding.ftueauth.AbstractFtueAuthFragment
import kotlinx.parcelize.Parcelize
import org.matrix.android.sdk.api.auth.data.LocalizedFlowDataLoginTerms
import javax.inject.Inject

@Parcelize
data class FtueAuthTermsLegacyStyleFragmentArgument(
        val localizedFlowDataLoginTerms: List<LocalizedFlowDataLoginTerms>
) : Parcelable

/**
 * LoginTermsFragment displays the list of policies the user has to accept.
 */
class FtueAuthLegacyStyleTermsFragment @Inject constructor(
        private val policyController: PolicyController
) : AbstractFtueAuthFragment<FragmentLoginTermsBinding>(),
        PolicyController.PolicyControllerListener {

    private val params: FtueAuthTermsLegacyStyleFragmentArgument by args()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginTermsBinding {
        return FragmentLoginTermsBinding.inflate(inflater, container, false)
    }

    private var loginTermsViewState: LoginTermsViewState = LoginTermsViewState(emptyList())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        views.loginTermsPolicyList.configureWith(policyController)
        policyController.listener = this

        val list = ArrayList<LocalizedFlowDataLoginTermsChecked>()

        params.localizedFlowDataLoginTerms
                .forEach {
                    list.add(LocalizedFlowDataLoginTermsChecked(it))
                }

        loginTermsViewState = LoginTermsViewState(list)
    }

    private fun setupViews() {
        views.loginTermsSubmit.setOnClickListener { submit() }
    }

    override fun onDestroyView() {
        views.loginTermsPolicyList.cleanup()
        policyController.listener = null
        super.onDestroyView()
    }

    private fun renderState() {
        policyController.setData(loginTermsViewState.localizedFlowDataLoginTermsChecked)
        views.loginTermsSubmit.isEnabled = loginTermsViewState.allChecked()
    }

    override fun setChecked(localizedFlowDataLoginTerms: LocalizedFlowDataLoginTerms, isChecked: Boolean) {
        if (isChecked) {
            loginTermsViewState.check(localizedFlowDataLoginTerms)
        } else {
            loginTermsViewState.uncheck(localizedFlowDataLoginTerms)
        }

        renderState()
    }

    override fun openPolicy(localizedFlowDataLoginTerms: LocalizedFlowDataLoginTerms) {
        localizedFlowDataLoginTerms.localizedUrl
                ?.takeIf { it.isNotBlank() }
                ?.let {
                    openUrlInChromeCustomTab(requireContext(), null, it)
                }
    }

    private fun submit() {
        viewModel.handle(OnboardingAction.PostRegisterAction(RegisterAction.AcceptTerms))
    }

    override fun updateWithState(state: OnboardingViewState) {
        policyController.homeServer = state.selectedHomeserver.userFacingUrl.toReducedUrl()
        renderState()
    }

    override fun resetViewModel() {
        viewModel.handle(OnboardingAction.ResetAuthenticationAttempt)
    }
}
