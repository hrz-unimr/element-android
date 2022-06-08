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

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec

/**
 * Helper to crate a [KeyStoreCrypto] compatible with APIs >= [Build.VERSION_CODES.M] or a legacy one.
 */
object KeyStoreCryptoCompat {

    fun create(context: Context, alias: String, keyGenParameterSpecBuilder: KeyGenParameterSpec.Builder.() -> Unit = {}): KeyStoreCrypto {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            KeyStoreCryptoLegacyImpl(context, alias)
        } else {
            KeyStoreCryptoImpl(alias, keyGenParameterSpecBuilder)
        }
    }
}
