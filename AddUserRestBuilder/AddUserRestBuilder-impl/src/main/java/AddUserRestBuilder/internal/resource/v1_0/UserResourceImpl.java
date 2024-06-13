package AddUserRestBuilder.internal.resource.v1_0;

import AddUserRestBuilder.dto.v1_0.NewUser;
import AddUserRestBuilder.dto.v1_0.UserObject;
import AddUserRestBuilder.resource.v1_0.UserResource;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.Locale;

/**
 * @author Jay
 */
@SuppressWarnings("unused")
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/user.properties",
	scope = ServiceScope.PROTOTYPE, service = UserResource.class
)
public class UserResourceImpl extends BaseUserResourceImpl {
    @Reference
    CounterLocalService counter;

    private static final Log _log = LogFactoryUtil.getLog(UserResourceImpl.class);

    @SuppressWarnings("unused")
    @Override
    public UserObject addUser(NewUser newUser) throws Exception {
        long companyId = contextCompany.getCompanyId();
        long creatorUserId = contextUser.getUserId();
//    	long creatorUserId = counter.increment();
        boolean autoPassword = false;
        String password1 = newUser.getPassword1();
        String password2 = newUser.getPassword2();
        boolean autoScreenName = false;
        String screenName = newUser.getScreenName();
        String emailAddress = newUser.getEmail();
        Locale locale = contextUser.getLocale();
        String firstName = newUser.getFirstName();
        String middleName = "";
        String lastName = newUser.getLastName();
        long prefixId = 0;
        long suffixId = 0;
        boolean male = true;
        int birthdayMonth = 1;
        int birthdayDay = 1; 
        int birthdayYear = 1970;
        int type = 1;
        String jobTitle = "";
        long[] groupIds = new long[0];
        long[] organizationIds = new long[0];
        long[] roleIds = new long[1];
        roleIds[0] = getRoleIdByName(newUser.getRole());
        long[] userGroupIds = new long[0];
        boolean sendEmail = true;

        ServiceContext serviceContext = new ServiceContext();
        User user = UserLocalServiceUtil.addUser(
            creatorUserId, companyId, autoPassword, password1, password2, 
            autoScreenName, screenName, emailAddress, locale, firstName, 
            middleName, lastName, prefixId, suffixId, male, birthdayMonth, 
            birthdayDay, birthdayYear, jobTitle, type, groupIds, organizationIds, 
            roleIds, userGroupIds, sendEmail, serviceContext
        );

        UserObject userObject = new UserObject();
        userObject.setUserId(user.getUserId());
        userObject.setScreenName(user.getScreenName());
        userObject.setFirstName(user.getFirstName());
        userObject.setLastName(user.getLastName());
        userObject.setEmail(user.getEmailAddress());
        userObject.setStatusCode(200);
        userObject.setStatusMessage("User added successfully");

        _log.info("Rest Api called");

        return userObject;
    }

    private long getRoleIdByName(String roleName) throws PortalException {
        Role role = RoleLocalServiceUtil.getRole(contextCompany.getCompanyId(), roleName);
        return role.getRoleId();
    }
}
