package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Group {

	private static final Logger log = LoggerFactory.getLogger(Group.class);
	private Long groupId;
	private String groupCode;
	private String description;
	private Tenant tenant;
	private Set<GroupAttribute> attributes = new HashSet<GroupAttribute>(0);
	private Set<GroupRole> roles = new HashSet<GroupRole>(0);

	@JsonIgnore
	private Integer version;
}
