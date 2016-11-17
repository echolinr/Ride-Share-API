/*
 * MongoLink, Object Document Mapper for Java and MongoDB
 *
 * You should have received a copy of the Lesser GNU General Public License
 * along with MongoLink.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.team4.uberapp.domain;

public abstract class Repositories {

    public static void initialise(Repositories instance) {
        Repositories.instance = instance;
    }

    public static CarRepository cars() {
        return instance.carsRepository();
    }

    public static DriverRepository drivers() {
        return instance.driversRepository();
    }

    public static PassengerRepository passengers() {
        return instance.passengersRepository();
    }

    public static RideRepository rides() {
        return instance.ridesRepository();
    }

    public static UserSessionRepository userSessions() {
        return instance.userSessionsRepository();
    }

    public static RoutePointRepository routePoints() {
        return instance.routePointsRepository();

    }

    protected abstract CarRepository carsRepository();
    protected abstract DriverRepository driversRepository();
    protected abstract PassengerRepository passengersRepository();
    protected abstract RideRepository ridesRepository();
    protected abstract UserSessionRepository userSessionsRepository();
    protected abstract RoutePointRepository routePointsRepository();

    private static Repositories instance;
}
