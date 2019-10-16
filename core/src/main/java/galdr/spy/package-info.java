/**
 * Spy events and introspection utilities.
 * The {@link galdr} package and this package are
 * highly inter-dependent but they are kept separated as production builds
 * typically compile out everything in this package when spies are disabled.
 */
@OmitPattern( unless = "galdr.enable_spies" )
package galdr.spy;

import grim.annotations.OmitPattern;
