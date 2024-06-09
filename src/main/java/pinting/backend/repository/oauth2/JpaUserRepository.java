package pinting.backend.repository.oauth2;

import jakarta.persistence.EntityManager;
import pinting.backend.entity.oauth2.UserEntity;

import java.util.List;
import java.util.Optional;

public class JpaUserRepository implements UserRepository {

	private final EntityManager em;

	public JpaUserRepository(EntityManager em) {
		this.em = em;
	}

	@Override
	public UserEntity save(UserEntity user) {
		em.persist(user);
		return user;
	}

	@Override
	public Optional<UserEntity> findById(Long id) {
		UserEntity user = em.find(UserEntity.class, id);
		return Optional.ofNullable(user);
	}

	@Override
	public Optional<UserEntity> findByName(String name) {
		List<UserEntity> result = em.createQuery("select m from UserEntity m where m.name = :name", UserEntity.class)
				.setParameter("name", name)
				.getResultList();

		return result.stream().findAny();
	}

	@Override
	public Optional<UserEntity> findByUsername(String username) {
		List<UserEntity> result = em.createQuery("select m from UserEntity m where m.username = :username", UserEntity.class)
				.setParameter("username", username)
				.getResultList();

		return result.stream().findAny();
	}

	@Override
	public List<UserEntity> findAll() {
		return em.createQuery("select m from UserEntity m", UserEntity.class)
				.getResultList();
	}
}
