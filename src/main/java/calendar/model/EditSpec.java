package calendar.model;

import java.time.LocalDateTime;

/**
 * Specifies modifications to an event using the Builder pattern.
 * Immutable after construction for thread safety.
 *
 * <p>DESIGN RATIONALE:
 * - Uses Builder pattern to avoid long parameter lists (code smell)
 * - Null values indicate "keep current value" semantics
 * - Immutable for thread safety and to prevent accidental modifications
 * - Builder provides fluent API for constructing edit specifications
 *
 * <p>Example usage:
 * 
 * <pre>
 * EditSpec spec = EditSpec.builder()
 *     .subject("New Subject")
 *     .start(LocalDateTime.now())
 *     .build();
 * </pre>
 */
public class EditSpec {
  private final String newSubject;
  private final LocalDateTime newStart;
  private final LocalDateTime newEnd;
  private final String newDescription;
  private final String newLocation;
  private final EventStatus newStatus;

  /**
   * Private constructor - use Builder to create instances.
   *
   * @param builder the builder containing the values
   */
  private EditSpec(Builder builder) {
    this.newSubject = builder.newSubject;
    this.newStart = builder.newStart;
    this.newEnd = builder.newEnd;
    this.newDescription = builder.newDescription;
    this.newLocation = builder.newLocation;
    this.newStatus = builder.newStatus;
  }

  /**
   * Creates an edit specification (legacy constructor for backward
   * compatibility).
   *
   * @param newSubject     the new subject, or null to keep current
   * @param newStart       the new start time, or null to keep current
   * @param newEnd         the new end time, or null to keep current
   * @param newDescription the new description, or null to keep current
   * @param newLocation    the new location, or null to keep current
   * @param newStatus      the new status, or null to keep current
   * @deprecated Use {@link #builder()} instead
   */
  @Deprecated
  public EditSpec(String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
      String newDescription, String newLocation, EventStatus newStatus) {
    this.newSubject = newSubject;
    this.newStart = newStart;
    this.newEnd = newEnd;
    this.newDescription = newDescription;
    this.newLocation = newLocation;
    this.newStatus = newStatus;
  }

  /**
   * Creates a new Builder for constructing EditSpec instances.
   *
   * @return a new Builder
   */
  public static Builder builder() {
    return new Builder();
  }

  public String getNewSubject() {
    return newSubject;
  }

  public LocalDateTime getNewStart() {
    return newStart;
  }

  public LocalDateTime getNewEnd() {
    return newEnd;
  }

  public String getNewDescription() {
    return newDescription;
  }

  public String getNewLocation() {
    return newLocation;
  }

  public EventStatus getNewStatus() {
    return newStatus;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof EditSpec)) {
      return false;
    }
    EditSpec other = (EditSpec) obj;
    return java.util.Objects.equals(newSubject, other.newSubject)
        && java.util.Objects.equals(newStart, other.newStart)
        && java.util.Objects.equals(newEnd, other.newEnd)
        && java.util.Objects.equals(newDescription, other.newDescription)
        && java.util.Objects.equals(newLocation, other.newLocation)
        && java.util.Objects.equals(newStatus, other.newStatus);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(newSubject, newStart, newEnd, newDescription, newLocation,
        newStatus);
  }

  /**
   * Builder class for creating EditSpec instances.
   * Provides a fluent API to set only the fields that need to be modified.
   */
  public static class Builder {
    private String newSubject;
    private LocalDateTime newStart;
    private LocalDateTime newEnd;
    private String newDescription;
    private String newLocation;
    private EventStatus newStatus;

    /**
     * Creates an empty Builder with all fields set to null.
     */
    Builder() {

    }

    /**
     * Sets the new subject for the event.
     *
     * @param subject the new subject
     * @return this Builder for chaining
     */
    public Builder subject(String subject) {
      this.newSubject = subject;
      return this;
    }

    /**
     * Sets the new start time for the event.
     *
     * @param start the new start time
     * @return this Builder for chaining
     */
    public Builder start(LocalDateTime start) {
      this.newStart = start;
      return this;
    }

    /**
     * Sets the new end time for the event.
     *
     * @param end the new end time
     * @return this Builder for chaining
     */
    public Builder end(LocalDateTime end) {
      this.newEnd = end;
      return this;
    }

    /**
     * Sets the new description for the event.
     *
     * @param description the new description
     * @return this Builder for chaining
     */
    public Builder description(String description) {
      this.newDescription = description;
      return this;
    }

    /**
     * Sets the new location for the event.
     *
     * @param location the new location
     * @return this Builder for chaining
     */
    public Builder location(String location) {
      this.newLocation = location;
      return this;
    }

    /**
     * Sets the new status (public/private) for the event.
     *
     * @param status the new status
     * @return this Builder for chaining
     */
    public Builder status(EventStatus status) {
      this.newStatus = status;
      return this;
    }

    /**
     * Builds and returns the EditSpec instance.
     *
     * @return a new EditSpec with the configured values
     */
    public EditSpec build() {
      return new EditSpec(this);
    }
  }
}