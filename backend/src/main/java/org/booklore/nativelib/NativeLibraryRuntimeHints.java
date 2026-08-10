package org.booklore.nativelib;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

/**
 * Embeds pdfium4j's bundled native libraries into the native image so they can be
 * extracted and {@code System.load}ed at runtime.
 *
 * <p>pdfium4j ships {@code libpdfium.so} + {@code pdfium4j_shim.so} inside its
 * {@code natives-<os>-<arch>} classifier jar under {@code natives/<os-arch>/} and reads
 * them via {@code getResourceAsStream} at init. A native image only embeds resources it
 * is told about; without these patterns the lookup returns null and PDFium fails to
 * initialize (PDF/comic rendering unavailable). Both patterns are needed: {@code natives/*}
 * for files directly under the dir and {@code natives/**} for the nested per-platform tree
 * (GraalVM globstar does not reliably cover depth-1 files) — same reasoning as the frontend
 * {@code static/**} hints in WebMvcConfig.
 *
 * <p>epub4j-native is intentionally not covered: its {@code .so} is not shipped in any
 * runtime artifact (only the sources jar), so it is unavailable on the JVM too — an upstream
 * packaging gap, not a native-image issue.
 */
@Configuration(proxyBeanMethods = false)
@ImportRuntimeHints(NativeLibraryRuntimeHints.PdfiumNativeHints.class)
public class NativeLibraryRuntimeHints {

    static class PdfiumNativeHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.resources().registerPattern("natives/*").registerPattern("natives/**");
        }
    }
}
