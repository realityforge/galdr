/**
 * This file provides the @defines for galdr configuration options.
 * See GaldrConfig.java for details.
 */
goog.provide('galdr');

/** @define {string} */
galdr.environment = goog.define('galdr.environment', 'production');

/** @define {string} */
galdr.enable_names = goog.define('galdr.enable_names', 'false');

/** @define {string} */
galdr.enable_spies = goog.define('galdr.enable_spies', 'false');

/** @define {string} */
galdr.enable_names = goog.define('galdr.copy_arrays_passed_to_constructors', 'false');

/** @define {string} */
galdr.debug_to_string = goog.define('galdr.debug_to_string', 'false');

/** @define {string} */
galdr.enable_error_handlers = goog.define('galdr.enable_error_handlers', 'true');

/** @define {string} */
galdr.check_invariants = goog.define('galdr.check_invariants', 'false');

/** @define {string} */
galdr.check_api_invariants = goog.define('galdr.check_api_invariants', 'false');

/** @define {string} */
galdr.logger = goog.define('galdr.logger', 'none');
