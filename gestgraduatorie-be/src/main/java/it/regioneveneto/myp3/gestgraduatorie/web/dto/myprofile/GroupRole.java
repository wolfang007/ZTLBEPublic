package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class GroupRole implements CreateAndUpdateTimestamp {
	private Long groupRoleId;

	private Group group;
	private Role role;
	private Set<Permission> permissions = new HashSet<Permission>(0);
	private Integer version;
	private Timestamp creationTimestamp;
	private Timestamp updateTimestamp;
}
