/*
 * Copyright (c) 2022 New Vector Ltd
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

package im.vector.app.features.pin.lockscreen.crypto

import im.vector.app.features.pin.lockscreen.LockScreenTestConstants
import im.vector.app.features.pin.lockscreen.LockScreenTestUtils
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class KeyStoreCryptoImplTests {

    private val keyStoreCryptoImpl = KeyStoreCryptoImpl(LockScreenTestConstants.ALIAS)

    @Before
    fun resetKeyStore() = LockScreenTestUtils.deleteKeyAlias(LockScreenTestConstants.ALIAS)

    @Test
    fun checkEncryptionAndDecryptionWorkAsExpected() {
        val original = "some plain text"
        val encryptedBytes = keyStoreCryptoImpl.encrypt(original)
        val result = keyStoreCryptoImpl.decryptToString(encryptedBytes)
        result shouldBeEqualTo original
    }
}
