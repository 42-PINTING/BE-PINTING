package pinting.backend.service.oauth2;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pinting.backend.controller.dto.oauth2.CustomOAuth2User;
import pinting.backend.controller.dto.oauth2.GoogleResponse;
import pinting.backend.controller.dto.oauth2.OAuth2Response;
import pinting.backend.controller.dto.oauth2.UserDto;
import pinting.backend.entity.oauth2.UserEntity;
import pinting.backend.repository.oauth2.UserRepository;

@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Autowired
	public CustomOAuth2UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = super.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		OAuth2Response oAuth2Response = null;

		if (registrationId.equals("google")) {
			oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
		} else {
			return null;
		}

		String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
		UserEntity existData = userRepository.findByUsername(username).orElse(null);

		if (existData == null) {
			UserEntity userEntity = new UserEntity();
			userEntity.setUsername(username);
			userEntity.setEmail(oAuth2Response.getEmail());
			userEntity.setName(oAuth2Response.getName());
			userEntity.setRole("ROLE_USER");

			userRepository.save(userEntity);

			UserDto userDto = new UserDto();
			userDto.setUsername(username);
			userDto.setName(oAuth2Response.getName());
			userDto.setRole("ROLE_USER");

			return new CustomOAuth2User(userDto);
		} else {
			existData.setEmail(oAuth2Response.getEmail());
			existData.setName(oAuth2Response.getName());

			userRepository.save(existData);

			UserDto userDto = new UserDto();
			userDto.setUsername(username);
			userDto.setName(oAuth2Response.getName());
			userDto.setRole(existData.getRole());

			return new CustomOAuth2User(userDto);
		}
	}
}